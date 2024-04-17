package game;

import java.util.*;

class Patch implements Comparable<Patch> {

    int i = 0;
    Patch parent = null;
    int n, m;
    char[][] p1;
    Position userPos = new Position();
    Position flag = new Position();
    int nnood;
    int cost;
    int h;

    void posflag_user() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (p1[i][j] == '☺') {
                    userPos.i = i;
                    userPos.j = j;
                }
                if (p1[i][j] == '♣') {
                    flag.i = i;
                    flag.j = j;
                }
            }
        }
    }

    public Patch() {
        char[][] level_1
                = {
                    {'☺', '■', '■', '■', '■', '♣'},
                    {'■', '■', '■', '■', '■', '■'}};
        char[][] level_2
                = {
                    {'☺', '■', '■', '□'},
                    {'■', '■', '◘', '♣'},
                    {'□', '□', '■', '□'}};
        char[][] level_8
             = {{'■', '■', '■', '■', '♣', '□'},
                {'■', '■', '■', '■', '◘', '■'},
                {'■', '■', '■', '■', '■', '■'},
                {'■', '■', '□', '■', '■', '■'},
                {'■', '◘', '■', '■', '■', '■'},
                {'□', '■', '■', '☺', '□', '□'}};
        this.p1 = level_8;
        this.n = p1.length;
        this.m = p1[0].length;
        nnood = 0;
        cost = 0;
        posflag_user();
        h = this.heurstic();
    }
    
    
    

    @Override
    public int compareTo(Patch t) {
        if (this.h < t.h) {
            return 1;
        }
        if (this.h > t.h) {
            return -1;
        }
        return 0;
    }

    public Patch deepCopy() {
        Patch pd = new Patch();
        pd.parent = this;
        pd.n = this.n;
        pd.m = this.m;
        pd.userPos.i = this.userPos.i;
        pd.userPos.j = this.userPos.j;
        pd.flag.i = this.flag.i;
        pd.flag.j = this.flag.j;
        pd.p1 = new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pd.p1[i][j] = this.p1[i][j];
            }
        }
        pd.i = this.i;
        return pd;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.parent);
        hash = 71 * hash + this.n;
        hash = 71 * hash + this.m;
        hash = 71 * hash + Arrays.deepHashCode(this.p1);
        hash = 71 * hash + Objects.hashCode(this.userPos);
        hash = 71 * hash + Objects.hashCode(this.flag);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Patch other = (Patch) obj;
        if (this.n != other.n) {
            return false;
        }
        if (this.m != other.m) {
            return false;
        }
        if (!Objects.equals(this.parent, other.parent)) {
            return false;
        }
        if (!Arrays.deepEquals(this.p1, other.p1)) {
            return false;
        }
        if (!Objects.equals(this.userPos, other.userPos)) {
            return false;
        }
        if (!Objects.equals(this.flag, other.flag)) {
            return false;
        }
        return true;
    }

    public int sumOfTrue() {
        int s = 1;
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < m; k++) {
                if (p1[j][k] == '□') {
                    s++;
                }
            }
        }
        return s;
    }

    public int heurstic() {
        int size = this.n * this.m;
        return (size - this.sumOfTrue());
    }

    public void show() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("|" + p1[i][j]);
            }
            System.out.print("|\n");
        }
        System.out.println("");
    }

    public boolean checkMove(Position newPos) {
        if ((newPos.i >= n || newPos.j >= m) || (newPos.i < 0 || newPos.j < 0) || (p1[newPos.i][newPos.j] == '□')) {
            return false;
        } else {
            return true;
        }
    }

    public void move(char dis) {

        Position newPos = userPos.clacPos(dis);
        boolean checkMove = checkMove(newPos);
        if (checkMove == false) {
            return;
        }
        if (p1[newPos.i][newPos.j] == '◘') {
            p1[userPos.i][userPos.j] = '□';
            p1[newPos.i][newPos.j] = '☺';
            userPos = newPos;
            this.i = 1;
            return;
        }
        if (i == 1) {
            p1[userPos.i][userPos.j] = '■';
            p1[newPos.i][newPos.j] = '☺';
            userPos = newPos;
            i = 0;
            return;
        }
        if (i == 0) {
            p1[userPos.i][userPos.j] = '□';
            p1[newPos.i][newPos.j] = '☺';
            userPos = newPos;
        }
        i = 0;
    }

    public boolean checkFinal() {
        if (userPos.i != flag.i || userPos.j != flag.j) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (p1[i][j] == '■') {
                    return false;
                }
            }
        }
        return true;
    }

    public Set<Patch> getNextState() {
        Set<Patch> p = new HashSet<>();
        Patch d1 = this.deepCopy();
        if (d1.checkMove(d1.userPos.clacPos('u'))) {
            d1.move('u');
            p.add(d1);
            d1.cost = ++d1.parent.cost;
            d1.h = d1.heurstic() + d1.cost;
        }
        Patch d2 = this.deepCopy();
        if (d2.checkMove(d2.userPos.clacPos('d'))) {
            d2.move('d');
            p.add(d2);
            d2.cost = ++d2.parent.cost;
            d2.h = d2.heurstic() + d2.cost;
        }
        Patch d3 = this.deepCopy();
        if (d3.checkMove(d3.userPos.clacPos('r'))) {
            d3.move('r');
            p.add(d3);
            d3.cost = ++d3.parent.cost;
            d3.h = d3.heurstic() + d3.cost;
        }
        Patch d4 = this.deepCopy();
        if (d4.checkMove(d4.userPos.clacPos('l'))) {
            d4.move('l');
            p.add(d4);
            d4.cost = ++d4.parent.cost;
            d4.h = d4.heurstic() + d4.cost;
        }
        return p;
    }

    public Patch dfsP() {
        Set<Patch> visited = new HashSet<>();
        Stack<Patch> st = new Stack<>();
        st.push(this);
        while (!st.isEmpty()) {
            this.nnood++;
            if (!visited.contains(st.peek())) {
                visited.add(st.peek());
                Patch p = st.pop();
                st.addAll(p.getNextState());
                if (p.checkFinal()) {
                    return p;
                }
            } else {
                st.pop();
            }
        }
        System.out.println("Sorry not found soulation");
        return this;

    }

    public Patch bfsP() {
        Set<Patch> visited = new HashSet<>();
        Queue<Patch> st = new LinkedList<>();
        st.add(this);
        while (!st.isEmpty()) {
            this.nnood++;
            if (!visited.contains(st.peek())) {
                visited.add(st.peek());
                Patch p = st.poll();
                st.addAll(p.getNextState());
                if (p.checkFinal()) {
                    return p;
                }
            } else {
                st.poll();
            }
        }
        System.out.println("Sorry not found soulation");
        return this;

    }

    public Patch ucsp() {
        Set<Patch> visited = new HashSet<>();
        PriorityQueue<Patch> st = new PriorityQueue<>();
        st.add(this);
        visited.add(this);
        while (!st.isEmpty()) {
            this.nnood++;
            Patch a = st.poll();
            Set<Patch> ls = a.getNextState();
            for (Patch g : ls) {
                if (g.checkFinal()) {
                    return g;
                }

                if (!visited.contains(g)) {
                    visited.add(g);
                    st.add(g);
                }
            }
        }
        System.out.println("Sorry not found soulation");
        return this;
    }

   

    
    
    
    public Patch aStarP() {
        HashSet<Patch> visited = new HashSet<>();
        PriorityQueue<Patch> st = new PriorityQueue<>();
        st.add(this);
        while (!st.isEmpty()) {
            if (!visited.contains(st.peek())) {
                visited.add(st.peek());
                Patch p = st.poll();
                this.nnood++;
                st.addAll(p.getNextState());
                if (p.checkFinal()) {
                    return p;
                }
            } else {
                st.poll();
                this.nnood++;
            }
        }
        System.out.println("Sorry not found soulation");
        return this;
    }
}

public class Game {

    public static void main(String[] args) {
       long startTime=System.nanoTime();
        Patch r = new Patch();
        // Patch e = r.dfsP();
        // Patch e = r.bfsP();
       // Patch e = r.ucsp();
        Patch e = r.aStarP();
       
        
        Stack<Patch> st = new Stack<>();
        int n = 0;
        while (e != null) {
            st.add(e);
            n++;
            e = e.parent;

        }
        while (!st.isEmpty()) {

            st.pop().show();
        }
        long endTime=System.nanoTime();
       long time=endTime-startTime;
        System.out.println("deep of soulation :" + --n);
        System.out.println("num of noods prossesing :" + r.nnood);
        System.out.println("time of rum :"+time/1000000+" mile second");

    }
}
