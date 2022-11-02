class Rectangle {
  // Function to print a rectangle
  // Input: n,m -> height, width
  // Output: rectangle printed
  // Time complexity: O(n*m)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int m = Integer.parseInt(args[1]);

    // Print rectangle
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= m; j++) {
        // If upper or lower border
        if (i == 1 || i == n) {
          System.out.print("+ ");
        // If left or right border
        } else if (j == 1 || j == m) {
          System.out.print("+ ");
        // If inside of rectangle
        } else {
          System.out.print("  ");
        }
      }
      System.out.println();
    }
  }
}