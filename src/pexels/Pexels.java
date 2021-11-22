package pexels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

// Escrito a prisas por Alejandro Ramos | @arhcoder.

public class Pexels extends javax.swing.JFrame
{
    // Attributes //
    File picture;
    byte[] image;
    String path;
    
    // Methods //
    // File Loading //
    public byte[] openImage(File file)
    {
        /// Accede al disco duro y obtiene un archivo que guarda en una secuencia de bytes.
        /// Regresa la secuencia de bits del archivo.
        
        byte[] bytesSequence = new byte[999999];
        
        try
        {
            FileInputStream inputFile = new FileInputStream(file);
            inputFile.read(bytesSequence);
        }
        catch (Exception FileNotFoundException)
        {
            JOptionPane.showMessageDialog(null, "¡No se pudo abrir!\nIntente de nuevo...\n" +
            FileNotFoundException, "Algo salió mal", JOptionPane.ERROR);
        }
        
        return bytesSequence;
    }
    public void drawImage(JLabel canvas)
    {
        /// Se busca y abre una imagen con el método [openImage].
        /// Se dibuja la imagen obtenida, dentro del Frame.
        
        // Abre un selector de archivos.
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showDialog(null, "Seleccione su archivo") == JFileChooser.APPROVE_OPTION)
        {
            // Filtra las extensiones de imagen.
            picture = fileChooser.getSelectedFile();
            if (picture.canRead())
            {
                if
                (
                    picture.getName().endsWith("jpg") ||
                    picture.getName().endsWith("jpeg") ||
                    picture.getName().endsWith("png") ||
                    picture.getName().endsWith("bmp")
                )
                {
                    // Abre la imagen seleccionada.
                    image = openImage(picture);
                    canvas.setIcon(new ImageIcon(image));

                    // Guarda la ruta de la imagen.
                    path = fileChooser.getSelectedFile().toString();
                    path = picture.getPath();

                    // Se ajusta la imágen al tamaño de la pestaña.
                    Toolkit tool = Toolkit.getDefaultToolkit();
                    Image RAMImage = tool.createImage(path);
                    
                    // Se escala a lo ancho si la imagen es más ancha que alta //
                    if(RAMImage.getWidth(this) > RAMImage.getHeight(this))
                    {
                        canvas.setIcon(new ImageIcon(RAMImage.getScaledInstance(canvas.getWidth(), -1, Image.SCALE_AREA_AVERAGING)));
                    }
                    // Se escala a lo alto si la imagene es más alta que ancha, o cuadrada //
                    else
                    {
                        canvas.setIcon(new ImageIcon(RAMImage.getScaledInstance(-1, canvas.getHeight(), Image.SCALE_AREA_AVERAGING)));
                    }
                    
                    // Se habilita la opción de guardar imágen.
                    Menu_File_Save.setEnabled(true);
                    Menu_File_SaveAs.setEnabled(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡No se pudo guardar!\nIntente de nuevo...\n",
                    "Algo salió mal", JOptionPane.ERROR);
                }
            }
        }
    }
    public void saveImage(File file, byte[] image)
    {
        /// Accede a memoria de disco duro y guarda un archivo imagen en la ruta especificada por el usuario.
        int confirmation = JOptionPane.showConfirmDialog(null, "¿Está seguro?\nSe sobreescribirá su archivo original si continúa...", "¿Desea guardar la imagen?", JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION)
        {
            try
            {
                FileOutputStream outputFile = new FileOutputStream(file);
                outputFile.write(image);
                JOptionPane.showMessageDialog(null, "Imágen guadada exitosamente");
            }
            catch (Exception FileNotFoundException)
            {
                JOptionPane.showMessageDialog(null, "¡No se pudo guardar!\nIntente de nuevo...\n" +
                FileNotFoundException, "Algo salió mal", JOptionPane.ERROR);
            }
        }   
    }
    public void saveImageAs()
    {
        
    }
    
    // Image manipulation //
    
    
    public Pexels()
    {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Picture = new javax.swing.JLabel();
        Menu = new javax.swing.JMenuBar();
        Menu_File = new javax.swing.JMenu();
        Menu_File_Open = new javax.swing.JMenuItem();
        Menu_File_Save = new javax.swing.JMenuItem();
        Menu_File_SaveAs = new javax.swing.JMenuItem();
        Menu_Operators = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pexels");
        setName("Pexels"); // NOI18N
        setSize(new java.awt.Dimension(1024, 800));

        Picture.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Picture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Picture.setAlignmentX(0.5F);

        Menu.setMinimumSize(new java.awt.Dimension(56, 100));

        Menu_File.setText("Archivo");

        Menu_File_Open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        Menu_File_Open.setText("Abrir Imagen");
        Menu_File_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_File_OpenActionPerformed(evt);
            }
        });
        Menu_File.add(Menu_File_Open);

        Menu_File_Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        Menu_File_Save.setText("Guardar");
        Menu_File_Save.setEnabled(false);
        Menu_File_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_File_SaveActionPerformed(evt);
            }
        });
        Menu_File.add(Menu_File_Save);

        Menu_File_SaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        Menu_File_SaveAs.setText("Guardar como");
        Menu_File_SaveAs.setEnabled(false);
        Menu_File.add(Menu_File_SaveAs);

        Menu.add(Menu_File);

        Menu_Operators.setText("Operadores");
        Menu.add(Menu_Operators);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Picture, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Picture, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Menu_File_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_File_OpenActionPerformed
        drawImage(Picture);
    }//GEN-LAST:event_Menu_File_OpenActionPerformed

    private void Menu_File_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_File_SaveActionPerformed
        saveImage(picture, image);
    }//GEN-LAST:event_Menu_File_SaveActionPerformed

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
    private javax.swing.JMenuBar Menu;
    private javax.swing.JMenu Menu_File;
    private javax.swing.JMenuItem Menu_File_Open;
    private javax.swing.JMenuItem Menu_File_Save;
    private javax.swing.JMenuItem Menu_File_SaveAs;
    private javax.swing.JMenu Menu_Operators;
    private javax.swing.JLabel Picture;
    // End of variables declaration//GEN-END:variables
}