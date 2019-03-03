package com.ittest.socketListener;

import com.ittest.socket.WIFIServiceSocket;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WIFIServerSocketListener implements ServletContextListener {

    private WIFIServiceSocket WIFIServiceSocket;

    /**
     * * Notification that the web application initialization
     * * process is starting.
     * * All ServletContextListeners are notified of context
     * * initialization before any filter or servlet in the web
     * * application is initialized.
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String str = (String) servletContext.getAttribute("socketThreadIsRun");
        if (str == null && WIFIServiceSocket == null) {
            servletContext.setAttribute("socketThreadIsRun", "true");
            WIFIServiceSocket = new WIFIServiceSocket(null, servletContext);
            WIFIServiceSocket.start(); //  servlet上下文初始化时启动socket服务端线程
        }

    }

    /**
     * * Notification that the servlet context is about to be shut down.
     * * All servlets and filters have been destroy()ed before any
     * * ServletContextListeners are notified of context
     * * destruction.
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (WIFIServiceSocket != null && !WIFIServiceSocket.isInterrupted()) {
            WIFIServiceSocket.closeSocketServer();
            WIFIServiceSocket.interrupt();
        }

    }
}
