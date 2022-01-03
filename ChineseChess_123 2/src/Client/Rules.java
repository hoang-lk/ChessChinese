package Client;

public class Rules {

    Model[][] chess;
    boolean canMove = false;

    int i;
    int j;

    public Rules(Model[][] chess) {
        this.chess = chess;
    }

    public boolean canMove(int startI, int startJ, int endI, int endJ, String name) {

        int maxI;
        int minI;
        int maxJ;
        int minJ;

        canMove = true;

        if (startI >= endI)
        {
            maxI = startI;
            minI = endI;
        } else
        {
            maxI = endI;
            minI = startI;
        }
        if (startJ >= endJ)
        {
            maxJ = startJ;
            minJ = endJ;
        } else {
            maxJ = endJ;
            minJ = startJ;
        }
        if (name.equals("車"))
        {
            this.Xe(maxI, minI, maxJ, minJ);
        } else if (name.equals("馬"))
        {
            this.Ma(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("相"))
        {
            this.Tinh1(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("象"))
        {
            this.Tinh2(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("士") || name.equals("仕"))
        {
            this.Si(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("帥") || name.equals("將"))
        {
            this.Tuong(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("炮") || name.equals("砲"))
        {
            this.Phao(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("兵"))
        {
            this.Tot1(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);

        } else if (name.equals("卒"))
        {
            this.Tot2(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        }
        return canMove;
    }

    public void Xe(int maxI, int minI, int maxJ, int minJ) {
        if (maxI == minI)
        {
            for (j = minJ + 1; j < maxJ; j++) {
                if (chess[maxI][j] != null)
                {
                    canMove = false;
                    break;
                }
            }
        } else if (maxJ == minJ)
        {
            for (i = minJ + 1; i < maxJ; i++) {
                if (chess[i][maxJ] != null)
                {
                    canMove = false;
                    break;
                }
            }
        } else if (maxI != minI && maxJ != minJ)
        {
            canMove = false;
        }
    }

    public void Ma(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if (a == 1 && b == 2)
        {
            if (startJ > endJ)
            {
                if (chess[startI][startJ - 1] != null)
                {
                    canMove = false;
                }
            } else{//Nếu nó đi từ trên xuống dưới
                if (chess[startI][startJ + 1] != null)//If there is a chess piece on the horse leg
                {
                    canMove = false;//Can't go
                }
            }
        } else if (a == 2 && b == 1)//nằm ngang
        {
            if (startI > endI)//If you go from right to left
            {
                if (chess[startI - 1][startJ] != null)//If there is a chess piece on the horse leg
                {
                    canMove = false;
                }
            } else {//If you go from left to right
                if (chess[startI + 1][startJ] != null)//If there is a chess piece on the horse leg
                {
                    canMove = false;
                }
            }
        } else if (!((a == 2 && b == 1) || (a == 1 && b == 2)))//If from time to time the word 
        {
            canMove = false;
        }
    }

    public void Tinh1(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if (a == 2 && b == 2)//
        {
            if (endJ > 4)//
            {
                canMove = false;//
            }
            if (chess[(maxI + minI) / 2][(maxJ + minJ) / 2] != null)//
            {
                canMove = false;//
            }
        } else {
            canMove = false;//
        }
    }

    public void Tinh2(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
        int a = maxI - minI;
        int b = maxJ - minJ;
        if (a == 2 && b == 2)
        {
            if (endJ < 5)
            {
                canMove = false;
            }
            if (chess[(maxI + minI) / 2][(maxJ + minJ) / 2] != null)
            {
                canMove = false;
            }
        } else {
            canMove = false;
        }
    }

    public void Si(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if (a == 1 && b == 1)//
        {
            if (startJ > 4)//
            {
                if (endJ < 7) {
                    canMove = false;//
                }
            } else {//
                if (endJ > 2) {
                    canMove = false;//
                }
            }
            if (endI > 5 || endI < 3)//
            {
                canMove = false;
            }
        } else {
            canMove = false;
        }
    }

    public void Tuong(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if ((a == 1 && b == 0) || (a == 0 && b == 1)) {
            if (startJ > 4)
            {
                if (endJ < 7)
                {
                    canMove = false;
                }
            } else {
                if (endJ > 2)
                {
                    canMove = false;
                }
            }
            if (endI > 5 || endI < 3)
            {
                canMove = false;
            }
        } else {
            canMove = false;
        }
    }

    public void Phao(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        if (maxI == minI)
        {
            if (chess[endI][endJ] != null)
            {
                int count = 0;
                for (j = minJ + 1; j < maxJ; j++) {
                    if (chess[minI][j] != null)
                    {
                        count++;
                    }
                }
                if (count != 1) {
                    canMove = false;
                }
            } else if (chess[endI][endJ] == null)
            {
                for (j = minJ + 1; j < maxJ; j++) {
                    if (chess[minI][j] != null)
                    {
                        canMove = false;
                        break;
                    }
                }
            }
        } else if (maxJ == minJ)
        {
            if (chess[endI][endJ] != null)
            {
                int count = 0;
                for (i = minI + 1; i < maxI; i++) {
                    if (chess[i][minJ] != null)
                    {
                        count++;
                    }
                }
                if (count != 1)
                {
                    canMove = false;
                }
            } else if (chess[endI][endJ] == null)
            {
                for (i = minI + 1; i < maxI; i++) {
                    if (chess[i][minJ] != null)
                    {
                        canMove = false;
                        break;
                    }
                }
            }
        } else if (maxJ != minJ && maxI != minI) {
            canMove = false;
        }
    }

    public void Tot1(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
        if (startJ < 5)
        {
            if (startI != endI)
            {
                canMove = false;
            }
            if (endJ - startJ != 1)
            {
                canMove = false;
            }
        } else {
            if (startI == endI) {
                if (endJ - startJ != 1)
                {
                    canMove = false;
                }
            } else if (startJ == endJ) {
                if (maxI - minI != 1) {
                    canMove = false;
                }
            } else if (startI != endI && startJ != endJ) {
                canMove = false;
            }
        }
    }

    public void Tot2(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
        if (startJ > 4) {
            if (startI != endI) {
                canMove = false;
            }
            if (endJ - startJ != -1)
            {
                canMove = false;
            }
        } else {
            if (startI == endI) {
                if (endJ - startJ != -1) {
                    canMove = false;
                }
            } else if (startJ == endJ) {
                if (maxI - minI != 1) {
                    canMove = false;
                }
            } else if (startI != endI && startJ != endJ) {
                canMove = false;
            }
        }
    }
}