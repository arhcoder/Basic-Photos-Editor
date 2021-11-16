package pexels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.*;

// Escrito a prisas por Alejandro Ramos | @arhcoder.

public class Pexels extends javax.swing.JFrame
{
    // Attributes //
    File picture;
    BufferedImage ramBuffer;
    
    // Methods //
    public byte[] openImage(File file)
    {
        /// Accede al disco duro y obtiene un archivo que guarda en una secuencia de bytes.
        /// Regresa la secuencia de bits del archivo.
        
        byte[] image = new byte[100 * 100];
        
        try
        {
            FileInputStream inputFile = new FileInputStream(file);
            inputFile.read(image);
        }
        catch (Exception FileNotFoundException)
        {
            JOptionPane.showMessageDialog(this, "¡No se pudo abrir!\nIntente de nuevo...\n" +
            FileNotFoundException, "Algo salió mal", JOptionPane.ERROR);
        }
        
        return image;
    }
    
    public void saveImage(File file, byte[] image)
    {
        /// Accede a memoria de disco duro y guarda un archivo imagen.
        
        try
        {
            FileOutputStream outputFile = new FileOutputStream(file);
            outputFile.write(image);
            JOptionPane.showMessageDialog(this, "Imágen guadada exitosamente");
        }
        catch (Exception FileNotFoundException)
        {
            JOptionPane.showMessageDialog(this, "¡No se pudo guardar!\nIntente de nuevo...\n" +
            FileNotFoundException, "Algo salió mal", JOptionPane.ERROR);
        }
    }
    
    public Pexels()
    {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[])
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pexels.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pexels.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pexels.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pexels.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Pexels().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}