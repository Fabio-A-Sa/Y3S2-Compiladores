01: t5 = 0             ->BB1
02: t0 = p1
03: t2 = 0
04: t1 = p2
05: if (t1 > 0) goto L1

06: t5 = t0             ->BB2
07: t0 = 1
08: t2 = 0

09: L1: t1 = t0         ->BB3
10: t4 = t0
11: if (t4 > t5) goto L2

12: t4 = t1 + 1        ->BB4
13: t2 = t2 + 1
14: goto L1

15: L2: t0 = p1       ->BB5
16: t3 = t0
17: t1 = t2 + t3
18: ret t1
