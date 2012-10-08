m = HMMFunction.getInitBirdModel(3,9, 0.00);
p = m.pi;

O = [2, 0, 0, 8, 6, 6, 0, 6, 6, 0, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 2, 3, 2, 5, 0, 0, 0, 4, 6, 6, 6, 6, 2, 5, 2, 3, 2, 5, 5, 5, 2, 5, 5, 2, 5, 5, 2, 5, 5, 0, 6, 7, 6, 6, 0, 7, 6, 6, 6, 6, 7, 6, 7, 6, 6, 6, 6, 7, 6, 6, 7, 2, 5, 5, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 7, 7, 6, 6, 6, 0, 6, 6, 6, 6, 6, 2, 5, 2, 2, 2, 3, 2, 5, 5, 2, 5, 5, 2, 2, 0, 0, 0, 0, 3, 6, 6, 6, 6, 6, 6, 7, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 3, 0, 7, 2, 5, 2, 2, 5, 5, 2, 5, 3, 5, 5, 5, 5, 2, 5, 5, 5, 2, 2, 5, 2, 0, 0, 3, 0, 0, 1, 8, 6, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 0, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 2, 8, 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1, 0, 0, 6, 6, 6, 6, 6, 7, 7, 6, 6, 2, 5, 2, 5, 0, 3, 0, 0, 8, 6, 6, 1, 6, 6, 6, 6, 7, 0, 1, 0, 0, 0, 0, 6, 7, 2, 0, 3, 0, 0, 0, 7, 6, 6, 6, 2, 3, 5, 5, 5, 5, 5, 2, 5, 5, 5, 5, 3, 6, 6, 6, 6, 6, 7, 6, 6, 2, 2, 5, 3, 2, 5, 2, 5, 0, 0, 1, 0, 0, 1, 8, 6, 6, 7, 6, 0, 7, 0, 6, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, 7, 0, 7, 6, 6, 6, 1, 6, 2, 5, 2, 2, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 4, 0, 0, 1, 0, 0, 1, 0, 0, 1, 3, 0, 0, 6, 7, 6, 6, 0, 0, 3, 0, 0, 3, 4, 3, 0, 0, 0, 0, 0, 3, 0, 0, 4, 3, 0, 0, 0, 3, 1, 8, 6, 6, 6, 6, 6, 2, 2, 2, 0, 5, 0, 6, 6, 6, 7, 7, 6, 6, 0, 1, 1, 0, 3, 0, 6, 6, 6, 6, 6, 6, 7, 6, 7, 6, 7, 6, 6, 7, 7, 6, 6, 7, 7, 6, 1, 4, 6, 0, 5, 5, 5, 6, 2, 5, 8, 0, 2, 6, 6, 6, 7, 6, 6, 6, 7, 0, 1, 3, 0, 3, 0, 0, 3, 3, 0, 6, 6, 0, 8, 2, 2];
O = O + 1;

Opart = O(1:100);

realA = [
    [0.85 0.15 0.00]
    [0.04 0.91 0.05]
    [0.08 0.04 0.88]
    ];
realB = [
    [0.61 0.14 0.00 0.15 0.04 0.00 0.01 0.00 0.05]
    [0.04 0.00 0.77 0.01 0.00 0.18 0.00 0.00 0.00]
    [0.03 0.06 0.01 0.00 0.00 0.00 0.43 0.45 0.02]
    ];
realB = [realB(:,1) realB(:,4) realB(:,7) realB(:,2) realB(:,5) realB(:,8) realB(:,3) realB(:,6) realB(:,9)];

% Major: Horizontal, minor: Vertical
% [0.61 0.15 0.01 0.14 0.04 0.00 0.00 0.00 0.05]
% [0.04 0.01 0.00 0.00 0.00 0.00 0.77 0.18 0.00]
% [0.03 0.00 0.43 0.06 0.00 0.45 0.01 0.00 0.02]

ballparkA = [
    [0.90 0.05 0.05]
    [0.01 0.85 0.14]
    [0.01 0.14 0.85]
    ];
ballparkB = [
    [0.51 0.10 0.00 0.20 0.09 0.00 0.05 0.00 0.05]
    [0.05 0.00 0.70 0.00 0.00 0.25 0.00 0.00 0.00]
    [0.10 0.19 0.00 0.00 0.00 0.00 0.33 0.33 0.05]
    ];
ballparkB = [ballparkB(:,1) ballparkB(:,4) ballparkB(:,7) ballparkB(:,2) ballparkB(:,5) ballparkB(:,8) ballparkB(:,3) ballparkB(:,6) ballparkB(:,9)];

% Major: Horizontal, minor: Vertical
% [0.51 0.20 0.05 0.10 0.09 0.00 0.00 0.00 0.05]
% [0.05 0.00 0.00 0.00 0.00 0.00 0.70 0.25 0.00]
% [0.10 0.00 0.33 0.19 0.00 0.33 0.00 0.00 0.05]

flatA = 1/3 * ones(3,3);
flatB = 1/9 * ones(3,9);
randomA = m.A;
randomB = m.B;

mobs = ModelledObservation(ObservationSequence(O-1), BirdModel(realA, realB, p))
mobs.refineModel(java.lang.System.currentTimeMillis() + 1000);
refinedAknowAknowB = mobs.lambda.A;
refinedBknowAknowB = mobs.lambda.B;

%%
% disp('---------------- Know A, flat B ----------------')
% [AknowAflatB,BknowAflatB] = hmmtrain(O,realA,flatB);
% MatrixMath.matrixNormDistance(realA, AknowAflatB)
% MatrixMath.matrixNormDistance(realB, BknowAflatB)
% disp('-------------------------------------------------')
%%
% disp('---------------- Flat A, know B ----------------')
% [AflatAknowB,BflatAknowB] = hmmtrain(O,flatA,realB);
% MatrixMath.matrixNormDistance(realA, AflatAknowB)
% MatrixMath.matrixNormDistance(realB, BflatAknowB)
% disp('-------------------------------------------------')
%%
disp('---------------- Flat A, Ballpark B ----------------')
[AflatAballparkB,BflatAballparkB] = hmmtrain(Opart,flatA,ballparkB);
MatrixMath.matrixNormDistance(realA, AflatAballparkB)
MatrixMath.matrixNormDistance(realB, BflatAballparkB)
disp('-------------------------------------------------')
%%
% disp('---------------- HMMFunction: Flat A, know B ----------------')
% MatrixMath.matrixNormDistance(realA, refinedAknowAknowB)
% MatrixMath.matrixNormDistance(realB, refinedBknowAknowB)
% disp('-------------------------------------------------')
%%
disp('---------------- HMMFunction: Flat A, Ballpark B ----------------')
MatrixMath.matrixNormDistance(realA, refinedAknowAballparkB)
MatrixMath.matrixNormDistance(realB, refinedBknowAballparkB)
disp('-------------------------------------------------')
%%
% disp('---------------- Know A, know B ----------------')
% [AknowAknowB,BknowAknowB] = hmmtrain(O,realA,realB);
% MatrixMath.matrixNormDistance(realA, AknowAknowB)
% MatrixMath.matrixNormDistance(realB, BknowAknowB)
% disp('-------------------------------------------------')
%%
% disp('---------------- flat A, flat B ----------------')
% [AflatAflatB,BflatAflatB] = hmmtrain(O,flatA,flatB);
% MatrixMath.matrixNormDistance(realA, AflatAflatB)
% MatrixMath.matrixNormDistance(realB, BflatAflatB)
% disp('-------------------------------------------------')
%%
% disp('---------------- Randomflat A, real B ----------------')
% [ArandomAknowB,BrandomAknowB] = hmmtrain(O,randomA,realB);
% MatrixMath.matrixNormDistance(realA, ArandomAknowB)
% MatrixMath.matrixNormDistance(realB, BrandomAknowB)
% disp('-------------------------------------------------')
%%
% disp('---------------- Know A, randomflat B ----------------')
% [AknowArandomB,BknowArandomB] = hmmtrain(O,realA,randomB);
% MatrixMath.matrixNormDistance(realA, AknowArandomB)
% MatrixMath.matrixNormDistance(realB, BknowArandomB)
% disp('-------------------------------------------------')
%%
% disp('---------------- Randomflat A, randomflat B ----------------')
% [ArandomArandomB,BrandomArandomB] = hmmtrain(O,randomA,randomB);
% MatrixMath.matrixNormDistance(realA, ArandomArandomB)
% MatrixMath.matrixNormDistance(realB, BrandomArandomB)
% disp('-------------------------------------------------')