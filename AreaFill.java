    
import java.util.*;
import java.awt.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class AreaFill {

    private List<Point> boundry;
    private List<Point> allboundrypoints;
    private Point[][] grid;
    private int[][] colorgrid;
    private static int ROWS;
    private static int COLUMNS;
    
    private static final int RED = 0,BLACK = 1,GREEN = 2;
    private static final int NEIGHBOR4 = 1,NEIGHBOR8 = 2;
    
    private int algorithm = NEIGHBOR4;

    // view attributes
    private int squareSize; // number of pixels in a square's edge
   
    private BufferedImage image;
    private ImageIcon imageIcon;
    private JLabel jLabel;
    private JFrame jFrame;

    public AreaFill(int r,int c) {
        ROWS = r;
        COLUMNS = c;
        // create an array list to store the line
        this.boundry = new ArrayList<>();
        this.allboundrypoints = new ArrayList<>();
        this.grid = new Point[r][c];
        this.colorgrid = new int[r][c];
        // initialize the grid mapping the XY scale
        for (int x = 0; x < r; x++){
            for (int y = 0; y < c; y++){
                this.grid[x][y] = new Point(x, y);
                // fill all pixel to red
                this.colorgrid[x][y] = RED;
            }
        }
    }
    
    private int getPixel(int x,int y){
        return this.colorgrid[x][y];
    }
    
    private void putPixel(int x, int y,int fill_color){
        this.colorgrid[x][y] = fill_color;
    }

    public void initFigure(){

        squareSize = getInt( "How many pixels per square? [1 - 100]? " );
        int imageSizeX = ROWS * squareSize;       
        int imageSizeY = COLUMNS * squareSize;       
        image = new BufferedImage( imageSizeY, imageSizeX, BufferedImage.TYPE_INT_ARGB );
        imageIcon = new ImageIcon( image );
        jLabel = new JLabel( imageIcon );
        jFrame = new JFrame( "Fill Area Algorithm");
        jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Container container = jFrame.getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( jLabel, BorderLayout.CENTER );
        jFrame.pack();
    }
    
    public void createLine(Point p0, Point p1) {  

        int x1=p0.x;
        int x2=p1.x;
        int y1=p0.y;
        int y2=p1.y;

        // delta of exact value and rounded value of the dependent variable
        int d = 0;
 
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        int x = x1;
        int y = y1;
 
        if (dx >= dy) {
            while (true) {
                
                this.allboundrypoints.add(this.grid[x][y]);
                // fill the boundary in black
                this.colorgrid[x][y] = BLACK;
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                
                this.allboundrypoints.add(this.grid[x][y]);
                 // fill the boundary in black
                this.colorgrid[x][y] = BLACK;
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }  
    }

    private int getInt( String question )
    {
        Scanner in = new Scanner(System.in);
        System.out.print(question);
        return in.nextInt();
    }

    private void view() { 
        jFrame.setVisible( true ); 
    }

    public static boolean isValidPoint(int x,int y){
        if((x >= ROWS) || (y >= COLUMNS)) return false;
        else if((x < 0) || (y < 0)) return false;
        return true;
    }

    public void drawBoundry(){

        initFigure();
        

        Graphics graphics = image.getGraphics();
        
        // paint a red  board
        graphics.setColor( Color.red );
        graphics.fillRect( 0, 0, COLUMNS * squareSize, ROWS * squareSize);
        
        // paint the black squares
        graphics.setColor( Color.black );
       
        for (int i = 0; i < ROWS; i++){
                
            for (int j = 0; j < COLUMNS; j++){

                if (this.allboundrypoints.contains(grid[i][j])){
                    graphics.fillRect( j* squareSize, i* squareSize, squareSize, squareSize );
                }
                
            }
           
        }
        
    }
    
    public void fillArea(Point start)throws ArrayIndexOutOfBoundsException{
        
        switch(algorithm){
            case NEIGHBOR4:
                boundaryFill4(start.x, start.y, GREEN);
                break;
            case NEIGHBOR8:
                boundaryFill8(start.x, start.y, GREEN);
                break;
        }

        Graphics graphics = image.getGraphics();
        
        // paint the green squares
        graphics.setColor( Color.GREEN );
       
        for (int i = 0; i < ROWS; i++){
                
            for (int j = 0; j < COLUMNS; j++){

                if (colorgrid[i][j]== GREEN){
                    graphics.fillRect( j* squareSize, i* squareSize, squareSize, squareSize );
                }
                
            }
           
        }
        
    }

    public void err(){
        System.out.println("The point you entered is out of the grid boundaries.");
        System.out.println("X scale: 0 to "+(ROWS-1));
        System.out.println("Y scale: 0 to "+(COLUMNS-1));
    }
    
    public void green2Red(){
        for (int i = 0; i < ROWS; i++){
                
            for (int j = 0; j < COLUMNS; j++){

                if (colorgrid[i][j]== GREEN){
                    colorgrid[i][j] = RED; 
                }
                
            }
           
        }
    }
    
    void boundaryFill4(int x, int y, int fill_color) throws ArrayIndexOutOfBoundsException{ 
        if(getPixel(x, y) != BLACK && getPixel(x, y) != fill_color) 
        { 
            putPixel(x, y, fill_color); 
            boundaryFill4(x + 1, y, fill_color); 
            boundaryFill4(x, y + 1, fill_color); 
            boundaryFill4(x - 1, y, fill_color); 
            boundaryFill4(x, y - 1, fill_color); 
        } 
    } 
    
    void boundaryFill8(int x, int y, int fill_color) throws ArrayIndexOutOfBoundsException{
        if(getPixel(x, y) != BLACK && getPixel(x, y) != fill_color)
        {
            putPixel(x, y, fill_color);
            boundaryFill8(x + 1, y, fill_color);
            boundaryFill8(x, y + 1, fill_color);
            boundaryFill8(x - 1, y, fill_color);
            boundaryFill8(x, y - 1, fill_color);
            boundaryFill8(x - 1, y - 1, fill_color);
            boundaryFill8(x - 1, y + 1, fill_color);
            boundaryFill8(x + 1, y - 1, fill_color);
            boundaryFill8(x + 1, y + 1, fill_color);
        }
    }

    public static void main(String[] args){
        // scanner to scann all the user inputs
        Scanner in = new Scanner(System.in);
        // print the topic of the application
        System.out.println("Fill Area Algorithm");

        
        System.out.print("\nEnter dimensions of grid: ");
        int rows = in.nextInt();
        int columns = in.nextInt();

        AreaFill  bresenham = new AreaFill(rows,columns);
        
        int points_boundry = 0;
        while(true){
            System.out.print("\nEnter the number of points of the boundry: ");
            points_boundry = in.nextInt();
            if(points_boundry>=3) break;
            else System.out.println("You need to click more than 2 points");
        }
        
        // create two points
        Point p = null;
        for (int i = 0; i < points_boundry; i++) {
            while(true){
                System.out.print("Enter the point "+(i+1)+" : ");
                int x = in.nextInt();
                int y = in.nextInt();
                if(isValidPoint(x, y)) {
                    p = new Point(x,y);
                    
                    bresenham.boundry.add(p);
                    break;
                }
                // print the error
                bresenham.err();
            }
        }
        
        
        // create all boundary lines
        int n = bresenham.boundry.size();
        for(int i = 0; i < n-1 ; i++){
            bresenham.createLine(bresenham.boundry.get(i), bresenham.boundry.get(i+1));
        }
        bresenham.createLine(bresenham.boundry.get(0), bresenham.boundry.get(n-1));
        
        
        // new line
        System.out.println();
        // draw the line
        bresenham.drawBoundry();
        
        // take the user supposed algorithm
        int algo = bresenham.getInt("Select your algorithm\n(1) 4-NEIGHBOR\n(2) 8-NEIGHBOR\n");
        if(algo==NEIGHBOR4 || algo==NEIGHBOR8)
            bresenham.algorithm = algo;
        
        // take the start point
        Point startPoint = null;
        boolean error = false;
        while(true){
            error = false;
            System.out.print("Enter a start point inside the boundry: ");
            int sx = in.nextInt();
            int sy = in.nextInt();
            if(isValidPoint(sx, sy)) {
                startPoint = new Point(sx, sy);
                bresenham.boundry.add(p);
                try{
                    bresenham.fillArea(startPoint);  
                }catch(IndexOutOfBoundsException ex){
                    System.out.println("The starting point you entered is out of the object boundary.");
                    // reset the color grid 
                    bresenham.green2Red();
                    error = true;
                }
                if(!error) break;
            }
            if(!error) bresenham.err();
            
        }
        
        
        // fill the area in green
        
        // view it
        bresenham.view();
        // close the scanner
        in.close();

    }        

}