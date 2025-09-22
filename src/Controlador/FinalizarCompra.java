package controlador;

import modelo.CarritoCompras;

public class FinalizarCompra implements Runnable {
    private final CarritoCompras carrito;
    private final ControladorPrincipal controlador;

    public FinalizarCompra(CarritoCompras carrito, ControladorPrincipal controlador) {
        this.carrito = carrito;
        this.controlador = controlador;
    }

    @Override
    public void run() {
        // Hilo 6: Simulación de pago y confirmación
        System.out.println("[Hilo 6 - Finalización] Procesando pago...");
        try {
            // Simula una demora por procesamiento de pago
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Hilo 6 - Finalización] Pago aprobado.");

        // Una vez finalizada la compra, se inicia el hilo para generar el ticket
        Thread hiloTicket = new Thread(new GenerarTicket(carrito, controlador), "HILO_TICKET");
        hiloTicket.start();
    }
}