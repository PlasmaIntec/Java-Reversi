import java.util.*;

public class ReversiBoard {

     protected char[][] board;
     protected char playerOnePiece;
     protected char playerTwoPiece;
     protected char emptyPiece;

     // CONSTRUCTORS
     public ReversiBoard() {
          board = new char[8][8];
          playerOnePiece = 'B';
          playerTwoPiece = 'W';
          emptyPiece = '_';
          for (int row = 0; row < board.length; row++) {
               for (int col = 0; col < board[row].length; col++) {
                    put(emptyPiece, new Point(row, col));
               }
          }
          put(playerOnePiece, new Point(3, 3));
          put(playerTwoPiece, new Point(3, 4));
          put(playerTwoPiece, new Point(4, 3));
          put(playerOnePiece, new Point(4, 4));
     }

     // PRINTING
     public void printBoard() {
          for (int row = 0; row < board.length; row++) {
               for (int col = 0; col < board[row].length; col++) {
                    System.out.printf("%s ", String.valueOf(board[row][col]));
               }
               System.out.println("\n");
          }
     }

     // ACCESSORS
     public char[][] getBoard() {
          return board;
     }

     public char at(Point point) {
          return board[point.getX()][point.getY()];
     }

     static public List<Point> getNeighboringPoints() {
          List<Point> points = new ArrayList<Point>();
          for (int x = -1; x < 2; x++) {
               for (int y = -1; y < 2; y++) {
                    Point Point = new Point(x, y);
                    points.add(Point);
               }
          }
          points.removeIf(value -> value.toString().equals("0@0"));
          return points;
     }

     public boolean hasAllyFromPointWithDirection(char piece, Point origin, Point direction) {
          Point current = origin.add(direction);
          while (current.getX() < board[0].length && current.getY() < board.length &&
               current.getX() >= 0 && current.getY() >= 0) {
               if (at(current) == piece) {
                    return true;
               }
               current = current.add(direction);
          }
          return false;
     }

     public Point getAllyFromPointWithDirection(char piece, Point origin, Point direction) {
          Point current = origin.add(direction);
          while (current.getX() < board[0].length && current.getY() < board.length &&
               current.getX() >= 0 && current.getY() >= 0) {
               if (at(current) == piece) {
                    return current;
               }
               current = current.add(direction);
          }
          return current; // should never reach this line
     }

     public List<Point> getSupportingAllies(char piece, Point origin) {
          List<Point> neighboringPoints = ReversiBoard.getNeighboringPoints();
          List<Point> supportingAllies = new ArrayList<Point>();
          for (Point neighbor : neighboringPoints) {
               if (hasAllyFromPointWithDirection(piece, origin, neighbor)) {
                    Point ally = getAllyFromPointWithDirection(piece, origin, neighbor);
                    supportingAllies.add(ally);
               }
          }
          return supportingAllies;
     }

     public List<Point> getPiecesInRange(Point origin, Point destination) {
          List<Point> pieces = new ArrayList<Point>();
          Point direction = origin.directionTo(destination);
          Point current = origin;
          while (!current.equals(destination)) {
               current = current.add(direction);
               pieces.add(current);
          }
          return pieces;
     }

     public int getNumberOfCaptures(char attacker, char defender, Point origin) {
          List<Point> supportingAllies = getSupportingAllies(attacker, origin);
          int captures = 0;
          for (Point ally : supportingAllies) {
               List<Point> pieces = getPiecesInRange(origin, ally);
               if (isUnbroken(attacker, defender, pieces)) {
                    for (Point piece : pieces) {
                         if (at(piece) == defender) {
                              captures++;
                         }
                    }
               }
          }
          return captures;
     }

     public List<Point> getLiveCells(char attacker, char defender) {
          List<Point> liveCells = new ArrayList<Point>();
          for (int row = 0; row < board.length; row++) {
               for (int col = 0; col < board[0].length; col++) {
                    Point point = new Point(row, col);
                    if (canPlaceAt(attacker, defender, point)) {
                         liveCells.add(point);
                    }
               }
          }
          return liveCells;
     }

     // TESTING
     public boolean isOccupiedAt(Point point) {
          return at(point) != emptyPiece;
     }

     public boolean isUnbroken(char attacker, char defender, List<Point> pieces) {
          for (Point piece : pieces) {
               if (at(piece) != attacker && at(piece) != defender) {
                    return false;
               }
          }
          return true;
     }

     public boolean hasAllySupportingAt(char ally, Point aPoint) {
          return getSupportingAllies(ally, aPoint).size() > 0;
     }

     public boolean hasNeighboringEnemyAt(char enemy, Point origin) {
          List<Point> neighboringPoints = ReversiBoard.getNeighboringPoints();
          for (Point neighbor : neighboringPoints) {
               Point point = origin.add(neighbor);
               if (point.getX() < board[0].length && point.getY() < board.length &&
                    point.getX() >= 0 && point.getY() >= 0 &&
                    at(point) == enemy) {
                    return true;
               }
          }
          return false;
     }
     
     public boolean canPlaceAt(char attacker, char defender, Point point) {
          return !isOccupiedAt(point) && 
               hasAllySupportingAt(attacker, point) && 
               hasNeighboringEnemyAt(defender, point) && 
               getNumberOfCaptures(attacker, defender, point) > 0;
     }

     // MUTATORS
     public void put(char piece, Point point) {
          board[point.getX()][point.getY()] = piece;
     }

     public void convertInRange(char piece, Point origin, Point destination) {
          Point direction = origin.directionTo(destination);
          Point current = origin;
          while (!current.equals(destination)) {
               put(piece, current);
               current = current.add(direction);
          }
          put(piece, current);
     }

     public void placeAt(char piece, Point point) {
          List<Point> supportingAllies = getSupportingAllies(piece, point);
          for (Point ally : supportingAllies) {
               convertInRange(piece, point, ally);
          }
     }

     // MAIN
     public static void main(String[] args) {
          ReversiBoard board = new ReversiBoard();
          board.placeAt('B', new Point(3, 5));
          board.printBoard();
          board.getLiveCells('W', 'B');
          // board.convertInRange('W', new Point(0, 0), new Point(0, 7));
          // board.put('B', new Point(3, 5));
          // board.put('B', new Point(3, 4));
          // board.printBoard();
          // board.getSupportingAllies('B', new Point(3, 5));
          // board.getNumberOfCaptures('W', 'B', new Point(2, 3));
          // System.out.println(board.canPlaceAt('W', 'B', new Point(4, 5)));
     }

}
