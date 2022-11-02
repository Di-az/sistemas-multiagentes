class Octagon {

  // Function to print a octagon
  // Input: n -> side size
  // Output: octagon printed
  // Time complexity: O(n^2)
  
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int outSpace = n - 1;
    int inSpace = n;

    // Upper line
    // Empty space
    for (int i = 0; i < outSpace; i++)
      System.out.print("  ");
    // Print side
    for (int i = 0; i < n; i++)
      System.out.print("+ ");
    System.out.println();
    outSpace -= 1;

    // Upper side
    for (int i = 0; i < n - 2; i++) {
      // Empty space
      for (int j = 0; j < outSpace; j++)
        System.out.print("  ");
      // Print side
      System.out.print("+ ");
      // Inner space
      for (int j = 0; j < inSpace; j++)
        System.out.print("  ");
      // Print side
      System.out.print("+ \n");
      outSpace -= 1;
      inSpace += 2;
    }

    // Middle
    for (int i = 0; i < n; i++) {
      // Print side
      System.out.print("+ ");
      // Inner space
      for (int j = 0; j < inSpace; j++) {
        System.out.print("  ");
      }
      // Print side
      System.out.print("+\n");
    }
    outSpace += 1;
    inSpace -= 2;

    // Lower side
    for (int i = 0; i < n - 2; i++) {
      // Empty space
      for (int j = 0; j < outSpace; j++)
        System.out.print("  ");
      // Print side
      System.out.print("+ ");
      // Inner space
      for (int j = 0; j < inSpace; j++)
        System.out.print("  ");
      // Print side
      System.out.print("+ \n");
      outSpace += 1;
      inSpace -= 2;
    }

    // Lower line
    // Empty space
    for (int i = 0; i < outSpace; i++)
      System.out.print("  ");
    // Print side
    for (int i = 0; i < n; i++)
      System.out.print("+ ");
    System.out.println();
  }
}