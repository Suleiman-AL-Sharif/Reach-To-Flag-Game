package game;

class Position {

    int i, j;

    Position clacPos(char d) {
        Position p2 = new Position();
        if (d == 'r') {
            p2.i = this.i;
            p2.j = this.j + 1;
        } else if (d == 'l') {
            p2.i = this.i;
            p2.j = this.j - 1;
        } else if (d == 'u') {
            p2.j = this.j;
            p2.i = this.i - 1;
        } else if (d == 'd') {
            p2.j = this.j;
            p2.i = this.i + 1;
        } else {
            p2.j = this.j;
            p2.i = this.i;
        }
        return p2;
    }
}
