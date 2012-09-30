m = TestLibrary.getModel_3x5()
A = m.A
B = m.B
p = m.pi

hmmgenerate()

%seq = SequenceGenerator.generateSequence(A, B, p, 500)

gA = [
        [0.8, 0.1, 0.1]
        [0.1, 0.8, 0.1]
        [0.6, 0.1, 0.3]
        ];
gB = [
        [0.05, 0.40, 0.30, 0.05, 0.20]
        [0.10, 0.10, 0.10, 0.10, 0.60]
        [0.30, 0.30, 0.10, 0.15, 0.15]
        ];

[ESTTR,ESTEMIT] = hmmtrain(seq,gA,gB);