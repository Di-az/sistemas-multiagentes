class Diamond {
  // Function to print a diamond
  // Input: n -> side size
  // Output: diamond printed
  // Time complexity: O(n^2)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int outSpace = n - 1;
    int inSpace = -1;

    // Upper corner
    for (int i = 0; i < outSpace; i++)
      System.out.print("  ");
    System.out.print("+ \n");
    outSpace -= 1;
    inSpace += 2;

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

    // Lower side
    for (int i = 0; i < n - 1; i++) {
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

    // Lower corner
    for (int i = 0; i < outSpace; i++)
      System.out.print("  ");
    System.out.print("+ \n");
  }
}