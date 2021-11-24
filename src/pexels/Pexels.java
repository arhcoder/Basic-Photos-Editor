package pexels;

import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import java.io.*;
import javax.imageio.ImageIO;

import javax.swing.filechooser.FileNameExtensionFilter;
import static java.lang.Math.pow;

/// Escrito a prisas por Alejandro Ramos | @arhcoder.
/// Versión 1.0.0: Noviembre 24, 2021.

public class Pexels extends javax.swing.JFrame
{
    // Attributes //
    File picture;
    byte[] image;
    String path;
    
    BufferedImage bufferedImage;
    int modifications = 0;
    
    // Filters //
    FileNameExtensionFilter imagesFilter = new FileNameExtensionFilter("Todas las imágenes","png", "jpg", "jpeg", "bmp");
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
        fileChooser.addChoosableFileFilter(imagesFilter);
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
                
                // Se habilitan los botones de filtros //
                Menu_Filter_Negative.setEnabled(true);
                Menu_Filter_BlackNWhite.setEnabled(true);
                Menu_Filter_Grays.setEnabled(true);
                Menu_Filter_Lighten.setEnabled(true);
                Menu_Filter_Darken.setEnabled(true);
                
                // Se habilitan los botones de bordes //
                Menu_Border_Laplace.setEnabled(true);
                Menu_Border_Prewitt.setEnabled(true);
                Menu_Border_Roberts.setEnabled(true);
                Menu_Border_Sobel.setEnabled(true);
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
    public void applyFilter(int filter, JLabel canvas)
    {
        /// Recibe un filtro [número entero], y en base a él, transforma la
        /// imágen y la redibuja en el canvas [JLabel].
        /// [0] = Negativo.
        /// [1] = Blanco y negro.
        /// [2] = Escala de grises.
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
            
            // Se elige el filtro //
            switch (filter)
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
                
                // Escala de grises //
                case 2:
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            r[x][y] = (short) ((r[x][y] + g[x][y] + b[x][y]) / 3);
                            g[x][y] = r[x][y];
                            b[x][y] = r[x][y];
                        }
                    }
                break;
                
                // Aclarar //
                case 3:
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            double r12 = r[x][y], g12 = g[x][y], b12 = b[x][y];
                            r[x][y] = (short) (255 * (pow(r12, 0.9) / pow(255, 0.9)));
                            g[x][y] = (short) (255 * (pow(g12, 0.9) / pow(255, 0.9)));
                            b[x][y] = (short) (255 * (pow(b12, 0.9) / pow(255, 0.9)));
                        }
                    }
                break;
                
                // Oscurecer //
                case 4:
                    for (int x = 0; x < width; x++)
                    {
                        for (int y = 0; y < height; y++)
                        {
                            double r12 = r[x][y], g12 = g[x][y], b12 = b[x][y];
                            r[x][y] = (short) (255 * (pow(r12, 1.1) / pow(255, 1.1)));
                            g[x][y] = (short) (255 * (pow(g12, 1.1) / pow(255, 1.1)));
                            b[x][y] = (short) (255 * (pow(b12, 1.1) / pow(255, 1.1)));
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
            
            // Se guarda la imágen de RAM en el atributo [image] de la clase.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            image = baos.toByteArray();
        }
        catch (Exception colorPrintException)
        {
            System.out.println("¡Ni idea de lo que sucedió!");
        }
    }
    
    public void drawBorders(int kind, JLabel canvas)
    {
        /// Recibe un tipo de borde [número entero], y en base a él, transforma
        /// la imágen y la redibuja en el canvas [JLabel].
        /// [0] = Laplace.
        /// [1] = Prewitt.
        /// [2] = Roberts.
        /// [3] = Sobel.
        
        try
        {
            // Se obtiene en RAM, el archivo que se desea manipular.
            File tempFile = new File(path);
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
            int[][] colorMatrix = new int[width][height];
            int maxGradient = -1;
            
            // Se elige el tipo de bordes //
            switch (kind)
            {
                // Laplace //
                case 0:
                    for (int i = 1; i < width - 1; i++)
                    {
                        for (int j = 1; j < height - 1; j++)
                        {
                            int val00 = getGrayScale(RAMImage.getRGB(i - 1, j - 1));
                            int val01 = getGrayScale(RAMImage.getRGB(i - 1, j));
                            int val02 = getGrayScale(RAMImage.getRGB(i - 1, j + 1));

                            int val10 = getGrayScale(RAMImage.getRGB(i, j - 1));
                            int val11 = getGrayScale(RAMImage.getRGB(i, j));
                            int val12 = getGrayScale(RAMImage.getRGB(i, j + 1));

                            int val20 = getGrayScale(RAMImage.getRGB(i + 1, j - 1));
                            int val21 = getGrayScale(RAMImage.getRGB(i + 1, j));
                            int val22 = getGrayScale(RAMImage.getRGB(i + 1, j + 1));
                    
                            int gx =  ((0 * val00) + (1 * val01) + (0 * val02)) 
                            + ((1 * val10) + (-4 * val11) + (1 * val12))
                            + ((0 * val20) + (1 * val21) + (0 * val22));

                            int gy =  ((0 * val00) + (-1 * val01) + (0 * val02))
                            + ((-1 * val10) + (4 * val11) + (-1 * val12))
                            + ((0 * val20) + (-1 * val21) + (0 * val22));

                            double grayValue = Math.sqrt((gx * gx) + (gy * gy));
                            int gray = (int) grayValue;
                            if(maxGradient < gray)
                            {
                                maxGradient = gray;
                            }
                            colorMatrix[i][j] = gray;
                        }
                    }
                break;
                
                // Prewitt //
                case 1:
                    for (int i = 1; i < width - 1; i++)
                    {
                        for (int j = 1; j < height - 1; j++)
                        {
                            int val00 = getGrayScale(RAMImage.getRGB(i - 1, j - 1));
                            int val01 = getGrayScale(RAMImage.getRGB(i - 1, j));
                            int val02 = getGrayScale(RAMImage.getRGB(i - 1, j + 1));

                            int val10 = getGrayScale(RAMImage.getRGB(i, j - 1));
                            int val11 = getGrayScale(RAMImage.getRGB(i, j));
                            int val12 = getGrayScale(RAMImage.getRGB(i, j + 1));

                            int val20 = getGrayScale(RAMImage.getRGB(i + 1, j - 1));
                            int val21 = getGrayScale(RAMImage.getRGB(i + 1, j));
                            int val22 = getGrayScale(RAMImage.getRGB(i + 1, j + 1));
                    
                            int gx =  ((-1 * val00) + (0 * val01) + (1 * val02)) 
                            + ((-1 * val10) + (0 * val11) + (1 * val12))
                            + ((-1 * val20) + (0 * val21) + (1 * val22));

                            int gy =  ((-1 * val00) + (-1 * val01) + (-1 * val02))
                            + ((0 * val10) + (0 * val11) + (0 * val12))
                            + ((1 * val20) + (1 * val21) + (1 * val22));
                            
                            double grayValue = Math.sqrt((gx * gx) + (gy * gy));
                            int gray = (int) grayValue;
                            if(maxGradient < gray)
                            {
                                maxGradient = gray;
                            }
                            colorMatrix[i][j] = gray;
                        }
                    }
                break;
                
                // Roberts //
                case 2:
                    for (int i = 1; i < width - 1; i++)
                    {
                        for (int j = 1; j < height - 1; j++)
                        {
                            int val00 = getGrayScale(RAMImage.getRGB(i - 1, j - 1));
                            int val01 = getGrayScale(RAMImage.getRGB(i - 1, j));

                            int val10 = getGrayScale(RAMImage.getRGB(i, j - 1));
                            int val11 = getGrayScale(RAMImage.getRGB(i, j));
                            
                            int gx =  ((-1 * val00) + (0 * val01)) 
                            + ((0 * val10) + (1 * val11));

                            int gy =  ((0 * val00) + (-1 * val01))
                            + ((1 * val10) + (0 * val11));
                            
                            double grayValue = Math.sqrt((gx * gx) + (gy * gy));
                            int gray = (int) grayValue;
                            if(maxGradient < gray)
                            {
                                maxGradient = gray;
                            }
                            colorMatrix[i][j] = gray;
                        }
                    }
                break;
                
                // Sobel //
                case 3:
                    for (int i = 1; i < width - 1; i++)
                    {
                        for (int j = 1; j < height - 1; j++)
                        {
                            int val00 = getGrayScale(RAMImage.getRGB(i - 1, j - 1));
                            int val01 = getGrayScale(RAMImage.getRGB(i - 1, j));
                            int val02 = getGrayScale(RAMImage.getRGB(i - 1, j + 1));

                            int val10 = getGrayScale(RAMImage.getRGB(i, j - 1));
                            int val11 = getGrayScale(RAMImage.getRGB(i, j));
                            int val12 = getGrayScale(RAMImage.getRGB(i, j + 1));

                            int val20 = getGrayScale(RAMImage.getRGB(i + 1, j - 1));
                            int val21 = getGrayScale(RAMImage.getRGB(i + 1, j));
                            int val22 = getGrayScale(RAMImage.getRGB(i + 1, j + 1));
                    
                            int gx =  ((-1 * val00) + (0 * val01) + (1 * val02)) 
                            + ((-2 * val10) + (0 * val11) + (2 * val12))
                            + ((-1 * val20) + (0 * val21) + (1 * val22));
                        
                            int gy =  ((-1 * val00) + (-2 * val01) + (-1 * val02))
                            + ((0 * val10) + (0 * val11) + (0 * val12))
                            + ((1 * val20) + (2 * val21) + (1 * val22));
                            
                            double grayValue = Math.sqrt((gx * gx) + (gy * gy));
                            int gray = (int) grayValue;
                            if(maxGradient < gray)
                            {
                                maxGradient = gray;
                            }
                            colorMatrix[i][j] = gray;
                        }
                    }
                break;
            }
            
            double scale = 255.0 / maxGradient;
            for (int i = 1; i < width - 1; i++)
            {
                for (int j = 1; j < height - 1; j++)
                {
                    int edgeColor = colorMatrix[i][j];
                    edgeColor = (int)(edgeColor * scale);
                    edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;
                    RAMImage.setRGB(i, j, edgeColor);
                }
            }
            bufferedImage = RAMImage;
            
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
            
            // Se guarda la imágen de RAM en el atributo [image] de la clase.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            image = baos.toByteArray();
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
        Menu_Filters = new javax.swing.JMenu();
        Menu_Filter_Negative = new javax.swing.JMenuItem();
        Menu_Filter_BlackNWhite = new javax.swing.JMenuItem();
        Menu_Filter_Grays = new javax.swing.JMenuItem();
        Menu_Filter_Lighten = new javax.swing.JMenuItem();
        Menu_Filter_Darken = new javax.swing.JMenuItem();
        Menu_Borders = new javax.swing.JMenu();
        Menu_Border_Laplace = new javax.swing.JMenuItem();
        Menu_Border_Prewitt = new javax.swing.JMenuItem();
        Menu_Border_Roberts = new javax.swing.JMenuItem();
        Menu_Border_Sobel = new javax.swing.JMenuItem();

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

        Menu_Filters.setText("Filtros");

        Menu_Filter_Negative.setText("Negativo");
        Menu_Filter_Negative.setEnabled(false);
        Menu_Filter_Negative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Filter_NegativeActionPerformed(evt);
            }
        });
        Menu_Filters.add(Menu_Filter_Negative);

        Menu_Filter_BlackNWhite.setText("Blanco y negro");
        Menu_Filter_BlackNWhite.setEnabled(false);
        Menu_Filter_BlackNWhite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Filter_BlackNWhiteActionPerformed(evt);
            }
        });
        Menu_Filters.add(Menu_Filter_BlackNWhite);

        Menu_Filter_Grays.setText("Escala de grises");
        Menu_Filter_Grays.setEnabled(false);
        Menu_Filter_Grays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Filter_GraysActionPerformed(evt);
            }
        });
        Menu_Filters.add(Menu_Filter_Grays);

        Menu_Filter_Lighten.setText("Aclarar");
        Menu_Filter_Lighten.setEnabled(false);
        Menu_Filter_Lighten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Filter_LightenActionPerformed(evt);
            }
        });
        Menu_Filters.add(Menu_Filter_Lighten);

        Menu_Filter_Darken.setText("Oscurecer");
        Menu_Filter_Darken.setEnabled(false);
        Menu_Filter_Darken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Filter_DarkenActionPerformed(evt);
            }
        });
        Menu_Filters.add(Menu_Filter_Darken);

        Menu.add(Menu_Filters);

        Menu_Borders.setText("Bordes");

        Menu_Border_Laplace.setText("Laplace");
        Menu_Border_Laplace.setEnabled(false);
        Menu_Border_Laplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Border_LaplaceActionPerformed(evt);
            }
        });
        Menu_Borders.add(Menu_Border_Laplace);

        Menu_Border_Prewitt.setText("Prewitt");
        Menu_Border_Prewitt.setEnabled(false);
        Menu_Border_Prewitt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Border_PrewittActionPerformed(evt);
            }
        });
        Menu_Borders.add(Menu_Border_Prewitt);

        Menu_Border_Roberts.setText("Roberts");
        Menu_Border_Roberts.setEnabled(false);
        Menu_Border_Roberts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Border_RobertsActionPerformed(evt);
            }
        });
        Menu_Borders.add(Menu_Border_Roberts);

        Menu_Border_Sobel.setText("Sobel");
        Menu_Border_Sobel.setEnabled(false);
        Menu_Border_Sobel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Menu_Border_SobelActionPerformed(evt);
            }
        });
        Menu_Borders.add(Menu_Border_Sobel);

        Menu.add(Menu_Borders);

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

    private void Menu_Filter_NegativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Filter_NegativeActionPerformed
        applyFilter(0, Picture);
    }//GEN-LAST:event_Menu_Filter_NegativeActionPerformed

    private void Menu_Filter_BlackNWhiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Filter_BlackNWhiteActionPerformed
        applyFilter(1, Picture);
    }//GEN-LAST:event_Menu_Filter_BlackNWhiteActionPerformed

    private void Menu_Filter_GraysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Filter_GraysActionPerformed
        applyFilter(2, Picture);
    }//GEN-LAST:event_Menu_Filter_GraysActionPerformed

    private void Menu_Filter_LightenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Filter_LightenActionPerformed
        applyFilter(3, Picture);
    }//GEN-LAST:event_Menu_Filter_LightenActionPerformed

    private void Menu_Filter_DarkenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Filter_DarkenActionPerformed
        applyFilter(4, Picture);
    }//GEN-LAST:event_Menu_Filter_DarkenActionPerformed

    private void Menu_Border_LaplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Border_LaplaceActionPerformed
        drawBorders(0, Picture);
    }//GEN-LAST:event_Menu_Border_LaplaceActionPerformed

    private void Menu_Border_PrewittActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Border_PrewittActionPerformed
        drawBorders(1, Picture);
    }//GEN-LAST:event_Menu_Border_PrewittActionPerformed

    private void Menu_Border_RobertsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Border_RobertsActionPerformed
        drawBorders(2, Picture);
    }//GEN-LAST:event_Menu_Border_RobertsActionPerformed

    private void Menu_Border_SobelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Menu_Border_SobelActionPerformed
        drawBorders(3, Picture);
    }//GEN-LAST:event_Menu_Border_SobelActionPerformed

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
    private javax.swing.JMenuItem Menu_Border_Laplace;
    private javax.swing.JMenuItem Menu_Border_Prewitt;
    private javax.swing.JMenuItem Menu_Border_Roberts;
    private javax.swing.JMenuItem Menu_Border_Sobel;
    private javax.swing.JMenu Menu_Borders;
    private javax.swing.JMenu Menu_File;
    private javax.swing.JMenuItem Menu_File_Open;
    private javax.swing.JMenuItem Menu_File_Reset;
    private javax.swing.JMenuItem Menu_File_Save;
    private javax.swing.JMenuItem Menu_File_SaveAs;
    private javax.swing.JMenuItem Menu_Filter_BlackNWhite;
    private javax.swing.JMenuItem Menu_Filter_Darken;
    private javax.swing.JMenuItem Menu_Filter_Grays;
    private javax.swing.JMenuItem Menu_Filter_Lighten;
    private javax.swing.JMenuItem Menu_Filter_Negative;
    private javax.swing.JMenu Menu_Filters;
    private javax.swing.JLabel Picture;
    // End of variables declaration//GEN-END:variables

    private int getGrayScale(int rgb)
    {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = (rgb) & 0xff;
        int gray = (int)(0.2126 * r + 0.7152 * g + 0.0722 * b);
        
        return gray;
    }
}