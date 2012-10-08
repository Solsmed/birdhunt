noiseModel = HMMFunction.getInitBirdModel(3,9,0.01);
noiseB = noiseModel.B;

% Xavi notation is
% Major: vertical
% Minor: horizontal
% Vaction = action / 3
% Haction = action % 3
%
% O = [2, 0, 0, 8, 6, 6, 0, 6, 6, 0, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 2, 3, 2, 5, 0, 0, 0, 4, 6, 6, 6, 6, 2, 5, 2, 3, 2, 5, 5, 5, 2, 5, 5, 2, 5, 5, 2, 5, 5, 0, 6, 7, 6, 6, 0, 7, 6, 6, 6, 6, 7, 6, 7, 6, 6, 6, 6, 7, 6, 6, 7, 2, 5, 5, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 7, 7, 6, 6, 6, 0, 6, 6, 6, 6, 6, 2, 5, 2, 2, 2, 3, 2, 5, 5, 2, 5, 5, 2, 2, 0, 0, 0, 0, 3, 6, 6, 6, 6, 6, 6, 7, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 3, 0, 7, 2, 5, 2, 2, 5, 5, 2, 5, 3, 5, 5, 5, 5, 2, 5, 5, 5, 2, 2, 5, 2, 0, 0, 3, 0, 0, 1, 8, 6, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 0, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 2, 8, 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1, 0, 0, 6, 6, 6, 6, 6, 7, 7, 6, 6, 2, 5, 2, 5, 0, 3, 0, 0, 8, 6, 6, 1, 6, 6, 6, 6, 7, 0, 1, 0, 0, 0, 0, 6, 7, 2, 0, 3, 0, 0, 0, 7, 6, 6, 6, 2, 3, 5, 5, 5, 5, 5, 2, 5, 5, 5, 5, 3, 6, 6, 6, 6, 6, 7, 6, 6, 2, 2, 5, 3, 2, 5, 2, 5, 0, 0, 1, 0, 0, 1, 8, 6, 6, 7, 6, 0, 7, 0, 6, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, 7, 0, 7, 6, 6, 6, 1, 6, 2, 5, 2, 2, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 4, 0, 0, 1, 0, 0, 1, 0, 0, 1, 3, 0, 0, 6, 7, 6, 6, 0, 0, 3, 0, 0, 3, 4, 3, 0, 0, 0, 0, 0, 3, 0, 0, 4, 3, 0, 0, 0, 3, 1, 8, 6, 6, 6, 6, 6, 2, 2, 2, 0, 5, 0, 6, 6, 6, 7, 7, 6, 6, 0, 1, 1, 0, 3, 0, 6, 6, 6, 6, 6, 6, 7, 6, 7, 6, 7, 6, 6, 7, 7, 6, 6, 7, 7, 6, 1, 4, 6, 0, 5, 5, 5, 6, 2, 5, 8, 0, 2, 6, 6, 6, 7, 6, 6, 6, 7, 0, 1, 3, 0, 3, 0, 0, 3, 3, 0, 6, 6, 0, 8, 2, 2];
%
% realB = [
%     [0.61 0.14 0.00 0.15 0.04 0.00 0.01 0.00 0.05]
%     [0.04 0.00 0.77 0.01 0.00 0.18 0.00 0.00 0.00]
%     [0.03 0.06 0.01 0.00 0.00 0.00 0.43 0.45 0.02]
%     ];
%
% conversion:
% realB = [realB(:,1) realB(:,4) realB(:,7) realB(:,2) realB(:,5) realB(:,8) realB(:,3) realB(:,6) realB(:,9)];

useXaviNotation = false;

O = [2, 0, 0, 8, 6, 6, 0, 6, 6, 0, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 2, 3, 2, 5, 0, 0, 0, 4, 6, 6, 6, 6, 2, 5, 2, 3, 2, 5, 5, 5, 2, 5, 5, 2, 5, 5, 2, 5, 5, 0, 6, 7, 6, 6, 0, 7, 6, 6, 6, 6, 7, 6, 7, 6, 6, 6, 6, 7, 6, 6, 7, 2, 5, 5, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 7, 7, 6, 6, 6, 0, 6, 6, 6, 6, 6, 2, 5, 2, 2, 2, 3, 2, 5, 5, 2, 5, 5, 2, 2, 0, 0, 0, 0, 3, 6, 6, 6, 6, 6, 6, 7, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 3, 0, 7, 2, 5, 2, 2, 5, 5, 2, 5, 3, 5, 5, 5, 5, 2, 5, 5, 5, 2, 2, 5, 2, 0, 0, 3, 0, 0, 1, 8, 6, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 0, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 2, 8, 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1, 0, 0, 6, 6, 6, 6, 6, 7, 7, 6, 6, 2, 5, 2, 5, 0, 3, 0, 0, 8, 6, 6, 1, 6, 6, 6, 6, 7, 0, 1, 0, 0, 0, 0, 6, 7, 2, 0, 3, 0, 0, 0, 7, 6, 6, 6, 2, 3, 5, 5, 5, 5, 5, 2, 5, 5, 5, 5, 3, 6, 6, 6, 6, 6, 7, 6, 6, 2, 2, 5, 3, 2, 5, 2, 5, 0, 0, 1, 0, 0, 1, 8, 6, 6, 7, 6, 0, 7, 0, 6, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, 7, 0, 7, 6, 6, 6, 1, 6, 2, 5, 2, 2, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 4, 0, 0, 1, 0, 0, 1, 0, 0, 1, 3, 0, 0, 6, 7, 6, 6, 0, 0, 3, 0, 0, 3, 4, 3, 0, 0, 0, 0, 0, 3, 0, 0, 4, 3, 0, 0, 0, 3, 1, 8, 6, 6, 6, 6, 6, 2, 2, 2, 0, 5, 0, 6, 6, 6, 7, 7, 6, 6, 0, 1, 1, 0, 3, 0, 6, 6, 6, 6, 6, 6, 7, 6, 7, 6, 7, 6, 6, 7, 7, 6, 6, 7, 7, 6, 1, 4, 6, 0, 5, 5, 5, 6, 2, 5, 8, 0, 2, 6, 6, 6, 7, 6, 6, 6, 7, 0, 1, 3, 0, 3, 0, 0, 3, 3, 0, 6, 6, 0, 8, 2, 2];
if useXaviNotation
    O = [6, 0, 0, 8, 2, 2, 0, 2, 2, 0, 2, 2, 2, 5, 2, 2, 2, 2, 2, 2, 2, 6, 1, 6, 7, 0, 0, 0, 4, 2, 2, 2, 2, 6, 7, 6, 1, 6, 7, 7, 7, 6, 7, 7, 6, 7, 7, 6, 7, 7, 0, 2, 5, 2, 2, 0, 5, 2, 2, 2, 2, 5, 2, 5, 2, 2, 2, 2, 5, 2, 2, 5, 6, 7, 7, 6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 2, 2, 5, 2, 2, 2, 5, 2, 2, 2, 5, 5, 2, 2, 2, 0, 2, 2, 2, 2, 2, 6, 7, 6, 6, 6, 1, 6, 7, 7, 6, 7, 7, 6, 6, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 5, 3, 0, 0, 0, 3, 0, 3, 3, 3, 0, 1, 0, 5, 6, 7, 6, 6, 7, 7, 6, 7, 1, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6, 7, 6, 0, 0, 1, 0, 0, 3, 8, 2, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 2, 0, 2, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 2, 2, 6, 8, 2, 2, 5, 5, 2, 2, 2, 5, 2, 2, 2, 5, 2, 2, 2, 0, 0, 0, 3, 0, 0, 0, 1, 0, 0, 3, 0, 0, 2, 2, 2, 2, 2, 5, 5, 2, 2, 6, 7, 6, 7, 0, 1, 0, 0, 8, 2, 2, 3, 2, 2, 2, 2, 5, 0, 3, 0, 0, 0, 0, 2, 5, 6, 0, 1, 0, 0, 0, 5, 2, 2, 2, 6, 1, 7, 7, 7, 7, 7, 6, 7, 7, 7, 7, 1, 2, 2, 2, 2, 2, 5, 2, 2, 6, 6, 7, 1, 6, 7, 6, 7, 0, 0, 3, 0, 0, 3, 8, 2, 2, 5, 2, 0, 5, 0, 2, 2, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 5, 0, 5, 2, 2, 2, 3, 2, 6, 7, 6, 6, 6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 4, 0, 0, 3, 0, 0, 3, 0, 0, 3, 1, 0, 0, 2, 5, 2, 2, 0, 0, 1, 0, 0, 1, 4, 1, 0, 0, 0, 0, 0, 1, 0, 0, 4, 1, 0, 0, 0, 1, 3, 8, 2, 2, 2, 2, 2, 6, 6, 6, 0, 7, 0, 2, 2, 2, 5, 5, 2, 2, 0, 3, 3, 0, 1, 0, 2, 2, 2, 2, 2, 2, 5, 2, 5, 2, 5, 2, 2, 5, 5, 2, 2, 5, 5, 2, 3, 4, 2, 0, 7, 7, 7, 2, 6, 7, 8, 0, 6, 2, 2, 2, 5, 2, 2, 2, 5, 0, 3, 1, 0, 1, 0, 0, 1, 1, 0, 2, 2, 0, 8, 6, 6];
end
O = O + 1; % Matlab indices

realA = [
    [0.85 0.15 0.00]
    [0.04 0.91 0.05]
    [0.08 0.04 0.88]
    ];
realB = [
    [0.61 0.15 0.01 0.14 0.04 0.00 0.00 0.00 0.05]
    [0.04 0.01 0.00 0.00 0.00 0.00 0.77 0.18 0.00]
    [0.03 0.00 0.43 0.06 0.00 0.45 0.01 0.00 0.02]
    ];
if useXaviNotation
swap = realB;
swap = [swap(:,1) swap(:,4) swap(:,7) swap(:,2) swap(:,5) swap(:,8) swap(:,3) swap(:,6) swap(:,9)];
realB = swap;
end

O = hmmgenerate(500,realA,realB);

ballparkA = [
    [0.88 0.06 0.06]
    [0.06 0.88 0.06]
    [0.06 0.06 0.88]
    ];
ballparkBall = [
    [0.51 0.20 0.05 0.10 0.09 0.00 0.00 0.00 0.05]
    [0.05 0.00 0.00 0.00 0.00 0.00 0.70 0.25 0.00]
    [0.10 0.00 0.33 0.19 0.00 0.33 0.00 0.00 0.05]
    [0.05 0.15 0.00 0.15 0.60 0.00 0.00 0.00 0.05]
    ];
ballparkB_PFM = [
    [0.51 0.20 0.05 0.10 0.09 0.00 0.00 0.00 0.05]
    [0.05 0.00 0.00 0.00 0.00 0.00 0.70 0.25 0.00]
    [0.10 0.00 0.33 0.19 0.00 0.33 0.00 0.00 0.05]
    ];
ballparkB_PFQ = [
    [0.51 0.20 0.05 0.10 0.09 0.00 0.00 0.00 0.05]
    [0.05 0.00 0.00 0.00 0.00 0.00 0.70 0.25 0.00]
    [0.05 0.15 0.00 0.15 0.60 0.00 0.00 0.00 0.05]
    ];
ballparkB_PMQ = [
    [0.51 0.20 0.05 0.10 0.09 0.00 0.00 0.00 0.05]
    [0.10 0.00 0.33 0.19 0.00 0.33 0.00 0.00 0.05]
    [0.05 0.15 0.00 0.15 0.60 0.00 0.00 0.00 0.05]
    ];
ballparkB_FMQ = [
    [0.05 0.00 0.00 0.00 0.00 0.00 0.70 0.25 0.00]
    [0.10 0.00 0.33 0.19 0.00 0.33 0.00 0.00 0.05]
    [0.05 0.15 0.00 0.15 0.60 0.00 0.00 0.00 0.05]
    ];

if useXaviNotation
swap = ballparkB;
swap = [swap(:,1) swap(:,4) swap(:,7) swap(:,2) swap(:,5) swap(:,8) swap(:,3) swap(:,6) swap(:,9)];
ballparkB = swap;
end
    
flatA = 1/3 * ones(3,3);
flatB = 1/9 * ones(3,9);

inputA = ballparkA;
inputB = ballparkB_PFM;
inputO = O(1:500);
%inputB = [inputB(:,1) inputB(:,4) inputB(:,7) inputB(:,2) inputB(:,5) inputB(:,8) inputB(:,3) inputB(:,6) inputB(:,9)];

%{
history = [];
historyLog = [];
historyIter = [];

STEP = 5;
for i = 1:STEP:500
    inputO = O(1:i);
    oldTrainedA = trainedA;
    oldTrainedB = trainedB;
    [trainedA,trainedB, logiks] = hmmtrain(inputO,inputA,inputB);
    dA = MatrixMath.matrixNormDistance(realA, trainedA);
    dB = MatrixMath.matrixNormDistance(realB, trainedB);
    deltadA = MatrixMath.matrixNormDistance(oldTrainedA, trainedA);
    deltadB = MatrixMath.matrixNormDistance(oldTrainedB, trainedB);
    disp(i);
    fill = [dA dB deltadA deltadB];
    fill = repmat(fill, [STEP,1]);
    history = [history; fill];
end
figure; plot(history)
%}

%%
inputB = ballparkB_PFM;
disp('---------------- Matlab: PFM ----------------')
history = [];
logHistory = [];

STEP = 1;
for i = 1:STEP:100
    inputO = O(1:i);
    oldTrainedA = trainedA;
    oldTrainedB = trainedB;
    [trainedA,trainedB, logiks] = hmmtrain(inputO,inputA,inputB);
    dA = MatrixMath.matrixNormDistance(realA, trainedA);
    dB = MatrixMath.matrixNormDistance(realB, trainedB);
    deltadA = MatrixMath.matrixNormDistance(oldTrainedA, trainedA);
    deltadB = MatrixMath.matrixNormDistance(oldTrainedB, trainedB);
    disp(i);
    fill = [dA dB deltadA deltadB];
    fill = repmat(fill, [STEP,1]);
    history = [history; fill];
    logHistory = [logHistory; max(logiks)];
end
figure;
subplot(2,1,1), plot(history)
subplot(2,1,2), plot(logHistory)
disp('-------------------------------------------------')
%{
inputB = ballparkB_PFQ;
disp('---------------- Matlab: PFQ ----------------')
[trainedA,trainedB] = hmmtrain(inputO,inputA,inputB);
MatrixMath.matrixNormDistance(realA, trainedA)
MatrixMath.matrixNormDistance(realB, trainedB)
disp('-------------------------------------------------')

inputB = ballparkB_PMQ;
disp('---------------- Matlab: PMQ ----------------')
[trainedA,trainedB] = hmmtrain(inputO,inputA,inputB);
MatrixMath.matrixNormDistance(realA, trainedA)
MatrixMath.matrixNormDistance(realB, trainedB)
disp('-------------------------------------------------')
%}
inputB = ballparkB_FMQ;
disp('---------------- Matlab: FMQ ----------------')
history = [];
logHistory = [];

STEP = 1;
for i = 1:STEP:100
    inputO = O(1:i);
    oldTrainedA = trainedA;
    oldTrainedB = trainedB;
    [trainedA,trainedB, logiks] = hmmtrain(inputO,inputA,inputB);
    dA = MatrixMath.matrixNormDistance(realA, trainedA);
    dB = MatrixMath.matrixNormDistance(realB, trainedB);
    deltadA = MatrixMath.matrixNormDistance(oldTrainedA, trainedA);
    deltadB = MatrixMath.matrixNormDistance(oldTrainedB, trainedB);
    disp(i);
    fill = [dA dB deltadA deltadB];
    fill = repmat(fill, [STEP,1]);
    history = [history; fill];
    logHistory = [logHistory; max(logiks)];
end
figure;
subplot(2,1,1), plot(history)
subplot(2,1,2), plot(logHistory)
disp('-------------------------------------------------')





%%
inputB = ballparkB_PFM;
disp('---------------- HMMFunction: PFM ----------------')
mobs = ModelledObservation(ObservationSequence(inputO-1), BirdModel(inputA, inputB, [1/3 1/3 1/3]));
mobs.refineModel(java.lang.System.currentTimeMillis() + 1000);
refinedA = mobs.lambda.A;
refinedB = mobs.lambda.B;
MatrixMath.matrixNormDistance(realA, refinedA)
MatrixMath.matrixNormDistance(realB, refinedB)
disp('-------------------------------------------------')

inputB = ballparkB_PFQ;
disp('---------------- HMMFunction: PFQ ----------------')
mobs = ModelledObservation(ObservationSequence(inputO-1), BirdModel(inputA, inputB, [1/3 1/3 1/3]));
mobs.refineModel(java.lang.System.currentTimeMillis() + 1000);
refinedA = mobs.lambda.A;
refinedB = mobs.lambda.B;
MatrixMath.matrixNormDistance(realA, refinedA)
MatrixMath.matrixNormDistance(realB, refinedB)
disp('-------------------------------------------------')

inputB = ballparkB_PMQ;
disp('---------------- HMMFunction: PMQ ----------------')
mobs = ModelledObservation(ObservationSequence(inputO-1), BirdModel(inputA, inputB, [1/3 1/3 1/3]));
mobs.refineModel(java.lang.System.currentTimeMillis() + 1000);
refinedA = mobs.lambda.A;
refinedB = mobs.lambda.B;
MatrixMath.matrixNormDistance(realA, refinedA)
MatrixMath.matrixNormDistance(realB, refinedB)
disp('-------------------------------------------------')

inputB = ballparkB_FMQ;
disp('---------------- HMMFunction: FMQ ----------------')
mobs = ModelledObservation(ObservationSequence(inputO-1), BirdModel(inputA, inputB, [1/3 1/3 1/3]));
mobs.refineModel(java.lang.System.currentTimeMillis() + 1000);
refinedA = mobs.lambda.A;
refinedB = mobs.lambda.B;
MatrixMath.matrixNormDistance(realA, refinedA)
MatrixMath.matrixNormDistance(realB, refinedB)
disp('-------------------------------------------------')
