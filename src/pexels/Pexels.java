package pexels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

// Escrito a prisas por Alejandro Ramos | @arhcoder.

public class Pexels extends javax.swing.JFrame
{
    // Attributes //
    File picture;
    byte[] image;
    String path;
    
    BufferedImage bufferedImage;
    int modifications = 0;
    
    // Filters //
    FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("Imágen PNG","png");
    FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("Imágen JPG","jpg");
    FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("Imágen JPEG","jpeg");
    FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("Imágen BMP","bmp");

    
    // Methods //
    // File Loading //
    public byte[] openImage(File file)
    {
        /// Accede al disco duro y obtiene un archivo que guarda en una secuencia de bytes.
        /// Regresa la secuencia de bits del archivo.
        
        try
        {
            FileInputStream inputFile = new FileInputStream(file);
            byte[] bytesSequence = new byte[(int)file.length()];
            inputFile.read(bytesSequence);
            
            return bytesSequence;
        }
        catch (Exception FileNotFoundException)
        {
            JOptionPane.showMessageDialog(null, "¡No se pudo abrir!\nIntente de nuevo...\n" +
            FileNotFoundException, "Algo salió mal", JOptionPane.ERROR);
            return null;
        }
    }
    public void drawImage(JLabel canvas)
    {
        /// Se busca y abre una imagen con el método [openImage].
        /// Se dibuja la imagen obtenida, dentro del Frame.
        
        // Abre un selector de archivos con filtro de imágenes.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.addChoosableFileFilter(jpegFilter);
        fileChooser.addChoosableFileFilter(bmpFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        if (fileChooser.showDialog(null, "Seleccione su archivo") == JFileChooser.APPROVE_OPTION)
        {
            // Filtra las extensiones de imagen.
            picture = fileChooser.getSelectedFile();
            modifications = 0;
            if (picture.canRead())
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
                Menu_File_Reset.setEnabled(true);
                
                // Se habilitan los botones de acción //
                Menu_Operator_Negative.setEnabled(true);
                Menu_Operator_BlackNWhite.setEnabled(true);

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
        JFileChooser explorer = new JFileChooser();
        explorer.setCurrentDirectory(new java.io.File("."));
        explorer.setDialogTitle("Seleccione una ubicación para guardar la imagen");
        explorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        explorer.addChoosableFileFilter(pngFilter);
        explorer.addChoosableFileFilter(jpgFilter);
        explorer.addChoosableFileFilter(jpegFilter);
        explorer.addChoosableFileFilter(bmpFilter);
        explorer.setAcceptAllFileFilterUsed(false);
        boolean writePermission = true;
        
        if (explorer.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            // Si se sobreescribirá un archivo //
           if (explorer.getSelectedFile().exists())
           {
               int confirmation = JOptionPane.showConfirmDialog(null, "¿Está seguro?\nSe sobreescribirá su archivo si continúa...", "¿Desea guardar la imagen?", JOptionPane.YES_NO_OPTION);
               if (confirmation == JOptionPane.NO_OPTION)
               {
                   writePermission = false;
               }
           }
           
           if (writePermission)
           {
               try
                {
                    // Obtiene la extensión colocada en el filtro del FileChooser.
                    javax.swing.filechooser.FileFilter currentFilter = explorer.getFileFilter();
                    String extension = ((FileNameExtensionFilter) currentFilter).getExtensions()[0];
                    
                    // Se guarda la imágen.
                    FileOutputStream outputFile = new FileOutputStream(explorer.getSelectedFile() + "." + extension);
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
    }
    
    public void resetImage(JLabel canvas)
    {
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
        modifications = 0;
    }
    
    // Image manipulation //
    public void operateImage(int operator, JLabel canvas)
    {
        /// Recibe un operador [entero], y en base a él, transforma la imágen y
        /// la redibuja en el canvas [JLabel].
        /// [0] = Negativo.
        /// [1] = Blanco y negro.
        /// [2] = Escala de grises
        /// [3] = Aclarar.
        /// [4] = Oscurecer.
        
        // Se obtiene en RAM, el archivo que se desea manipular.
        File tempFile = new File(path);
        
        // Se intenta el cambio de colores.
        try
        {
            BufferedImage RAMImage = ImageIO.read(tempFile);
            if (modifications == 0)
            {
                modifications++;
            }
            else
            {
                RAMImage = bufferedImage;
            }
            
            int width = RAMImage.getWidth();
            int height = RAMImage.getHeight();
            int rgbArray[] = new int[width * height];
            
            RAMImage.getRGB(0, 0, width, height, rgbArray, 0, width);
            ColorModel cm = ColorModel.getRGBdefault(); 		
            
            short r[][] = new short[width][height];
            short g[][] = new short[width][height];
            short b[][] = new short[width][height];
            
            int i = 0;

            // Se descompone la imágen en RGB en un arreglo.
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    i = x + y * width;
                    b[x][y] = (short) cm.getBlue(rgbArray[i]);
                    g[x][y] = (short) cm.getGreen(rgbArray[i]);
                    r[x][y] = (short) cm.getRed(rgbArray[i]);
                }
            }
            
            // Se elige el operador //
            switch (operator)
            {
                // Negativo //
                case 0:
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            r[x][y] = (short) (255 - r[x][y]);
                            g[x][y] = (short) (255 - g[x][y]);
                            b[x][y] = (short) (255 - b[x][y]);
                        }
                    }
                break;
                
                // Blanco y negro //
                case 1:
                    double medium;
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            double r12 = r[x][y], g12 = g[x][y], b12 = b[x][y];
                            medium = (r12 + g12 + b12) / 3;
                            if (medium > 128)
                            {
                                r[x][y] = (short) (255);
                                g[x][y] = (short) (255);
                                b[x][y] = (short) (255);
                            }
                            else
                            {
                                r[x][y] = (short) (0);
                                g[x][y] = (short) (0);
                                b[x][y] = (short) (0);
                            }
                        }
                    }
                break;
            }
            
            // Se convierte el arreglo.
            int array[] = new int[width * height];
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    array[x + y * width]
                    = 0xff000000
                    | (r[x][y] << 16)
                    | (g[x][y] << 8)
                    | b[x][y];
                }
            }

            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
            bufferedImage.setRGB(0, 0, width, height, array, 0, width);
            
            // Se escala a lo ancho si la imagen es más ancha que alta //
            if(bufferedImage.getWidth(this) > bufferedImage.getHeight(this))
            {
                canvas.setIcon(new ImageIcon(bufferedImage.getScaledInstance(canvas.getWidth(), -1, Image.SCALE_AREA_AVERAGING)));
            }
            // Se escala a lo alto si la imagene es más alta que ancha, o cuadrada //
            else
            {
                canvas.setIcon(new ImageIcon(bufferedImage.getScaledInstance(-1, canvas.getHeight(), Image.SCALE_AREA_AVERAGING)));
            }
            RAMImage = bufferedImage;
        }
        catch (Exception colorPrintException)
        {
            System.out.println("¡Ni idea de lo que sucedió!");
        }
    }
    
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
        Menu_File_Reset = new javax.swing.JMenuItem();
        Menu_Operators = new javax.swing.JMenu();
        Menu_Operator_Negative = new javax.swing.JMenuItem();
        Menu_Operator_BlackNWhite = new javax.swing.JMenuItem();
        Menu_Operator_Greys = new javax.swing.JMenuItem();
        Menu_Operator_Lighten = new javax.swing.JMenuItem();
        Menu_Operator_Darken = new javax.swing.JMenuItem();

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
        Menu_File_SaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_File_SaveAsActionPerformed(evt);
            }
        });
        Menu_File.add(Menu_File_SaveAs);

        Menu_File_Reset.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        Menu_File_Reset.setText("Restaurar");
        Menu_File_Reset.setEnabled(false);
        Menu_File_Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_File_ResetActionPerformed(evt);
            }
        });
        Menu_File.add(Menu_File_Reset);

        Menu.add(Menu_File);

        Menu_Operators.setText("Operadores");

        Menu_Operator_Negative.setText("Negativo");
        Menu_Operator_Negative.setEnabled(false);
        Menu_Operator_Negative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Operator_NegativeActionPerformed(evt);
            }
        });
        Menu_Operators.add(Menu_Operator_Negative);

        Menu_Operator_BlackNWhite.setText("Blanco y negro");
        Menu_Operator_BlackNWhite.setEnabled(false);
        Menu_Operator_BlackNWhite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Operator_BlackNWhiteActionPerformed(evt);
            }
        });
        Menu_Operators.add(Menu_Operator_BlackNWhite);

        Menu_Operator_Greys.setText("Escala de grises");
        Menu_Operator_Greys.setEnabled(false);
        Menu_Operator_Greys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Operator_GreysActionPerformed(evt);
            }
        });
        Menu_Operators.add(Menu_Operator_Greys);

        Menu_Operator_Lighten.setText("Aclarar");
        Menu_Operator_Lighten.setEnabled(false);
        Menu_Operator_Lighten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Operator_LightenActionPerformed(evt);
            }
        });
        Menu_Operators.add(Menu_Operator_Lighten);

        Menu_Operator_Darken.setText("Oscurecer");
        Menu_Operator_Darken.setEnabled(false);
        Menu_Operator_Darken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Operator_DarkenActionPerformed(evt);
            }
        });
        Menu_Operators.add(Menu_Operator_Darken);

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

    private void Menu_File_SaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_File_SaveAsActionPerformed
        saveImageAs();
    }//GEN-LAST:event_Menu_File_SaveAsActionPerformed

    private void Menu_File_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_File_ResetActionPerformed
        resetImage(Picture);
    }//GEN-LAST:event_Menu_File_ResetActionPerformed

    private void Menu_Operator_NegativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Operator_NegativeActionPerformed
        operateImage(0, Picture);
    }//GEN-LAST:event_Menu_Operator_NegativeActionPerformed

    private void Menu_Operator_BlackNWhiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Operator_BlackNWhiteActionPerformed
        operateImage(1, Picture);
    }//GEN-LAST:event_Menu_Operator_BlackNWhiteActionPerformed

    private void Menu_Operator_GreysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Operator_GreysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Menu_Operator_GreysActionPerformed

    private void Menu_Operator_LightenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Operator_LightenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Menu_Operator_LightenActionPerformed

    private void Menu_Operator_DarkenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Operator_DarkenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Menu_Operator_DarkenActionPerformed

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
    private javax.swing.JMenuItem Menu_File_Reset;
    private javax.swing.JMenuItem Menu_File_Save;
    private javax.swing.JMenuItem Menu_File_SaveAs;
    private javax.swing.JMenuItem Menu_Operator_BlackNWhite;
    private javax.swing.JMenuItem Menu_Operator_Darken;
    private javax.swing.JMenuItem Menu_Operator_Greys;
    private javax.swing.JMenuItem Menu_Operator_Lighten;
    private javax.swing.JMenuItem Menu_Operator_Negative;
    private javax.swing.JMenu Menu_Operators;
    private javax.swing.JLabel Picture;
    // End of variables declaration//GEN-END:variables

    private String getExtensionForFilter(javax.swing.filechooser.FileFilter fileFilter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}