package club.tesseract.extendedviewdistance.core.data.viewmap;

public enum ViewShape {
  SQUARE((aX, aZ, bX, bZ, viewDistance) -> {
    int minX = bX - viewDistance;
    int minZ = bZ - viewDistance;
    int maxX = bX + viewDistance;
    int maxZ = bZ + viewDistance;
    return aX >= minX && aZ >= minZ && aX <= maxX && aZ <= maxZ;
  }),
  ROUND(
    (aX, aZ, bX, bZ, viewDistance) -> {
      int viewDiameter = viewDistance * viewDistance + viewDistance;
      int distanceX = aX - bX;
      int distanceZ = aZ - bZ;
      int distance = distanceX * distanceX + distanceZ * distanceZ;
      return distance <= viewDiameter;
    },
    (aX, aZ, bX, bZ, viewDistance) -> {
      JudgeInside inside = (_aX, _aZ, _bX, _bZ, viewDiameterx) -> {
        int distanceX = _aX - _bX;
        int distanceZ = _aZ - _bZ;
        int distance = distanceX * distanceX + distanceZ * distanceZ;
        return distance <= viewDiameterx;
      };
      int viewDiameter = viewDistance * viewDistance + viewDistance;
      return (
        inside.test(aX, aZ, bX, bZ, viewDiameter) &&
        inside.test(aX + 1, aZ, bX, bZ, viewDiameter) &&
        inside.test(aX - 1, aZ, bX, bZ, viewDiameter) &&
        inside.test(aX, aZ + 1, bX, bZ, viewDiameter) &&
        inside.test(aX, aZ - 1, bX, bZ, viewDiameter)
      );
    }
  );

  private final JudgeInside judgeInside;
  private final JudgeInside judgeInsideEdge;

  private ViewShape(JudgeInside judgeInside) {
    this(judgeInside, judgeInside);
  }

  private ViewShape(
    JudgeInside judgeInside,
    JudgeInside judgeInsideEdge
  ) {
    this.judgeInside = judgeInside;
    this.judgeInsideEdge = judgeInsideEdge;
  }

  public boolean isInside(int aX, int aZ, int bX, int bZ, int viewDistance) {
    return this.judgeInside.test(aX, aZ, bX, bZ, viewDistance);
  }

  public boolean isInsideEdge(
    int aX,
    int aZ,
    int bX,
    int bZ,
    int viewDistance
  ) {
    return this.judgeInsideEdge.test(aX, aZ, bX, bZ, viewDistance);
  }

  // $FF: synthetic method
  private static ViewShape[] $values() {
    return new ViewShape[] { SQUARE, ROUND };
  }

  interface JudgeInside {
    boolean test(int var1, int var2, int var3, int var4, int var5);
  }
}
