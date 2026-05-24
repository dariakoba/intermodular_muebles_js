package com.dna.muebles.service;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import com.dna.muebles.db.Tx; 
import com.dna.muebles.dto.CarritoRequest;
import com.dna.muebles.entity.Pedido;
import com.dna.muebles.repository.PedidoRepository;
import com.dna.muebles.repository.ProductoRepository; 
import com.dna.muebles.repository.UserRepository;

public class PedidoService {
    
  
    private final DataSource ds;

    public PedidoService(DataSource ds) {
        this.ds = ds;
    }

    public void realizarCompra(Integer userId, CarritoRequest request, int puntosAUsar, int puntosGanados) {
        
        
        Tx.run(ds, new Function<Connection, Boolean>() {
            
            @Override
            public Boolean apply(Connection c) {
                try {
                    // 1. Instanciar los repositorios
                    UserRepository userRepo = new UserRepository(c);
                    PedidoRepository pedidoRepo = new PedidoRepository(c);
                    ProductoRepository productoRepo = new ProductoRepository(c); 
                    
                    // 2. Actualizar Usuario
                    if (request.getDireccion() != null && !request.getDireccion().isEmpty()) {
                        userRepo.actualizarDireccion(userId, request.getDireccion());
                    }
                    userRepo.actualizarPuntos(userId, puntosAUsar, puntosGanados);

                    // 3. Crear el Pedido
                    Pedido p = request.getPedido();
                    p.setIdUsuario(userId);
                    p.setFecha(LocalDate.now());
                    p.setPuntosUsados(puntosAUsar);
                    Pedido nuevoPedido = pedidoRepo.insert(p);
                    int idGenerado = nuevoPedido.getIdPedido();

                    // 4. Recorrer Carrito
                    for (Map<String, Object> item : request.getProductos()) {
                        int idProd = Integer.parseInt(item.get("id_producto").toString());
                        int cant = Integer.parseInt(item.get("cantidad").toString());
                        float precio = Float.parseFloat(item.get("precio").toString());
                        
                        //  descontar el stock
                        int filasActualizadas = productoRepo.decrementStock(idProd, cant);
                        
                        if (filasActualizadas == 0) {
                            throw new RuntimeException("No hay stock suficiente para el producto con ID: " + idProd);
                        }

                        pedidoRepo.guardarDetalle(idGenerado, idProd, cant, precio);
                    }

                    return true; 
                    
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage()); 
                }
            }
        });
    }
}