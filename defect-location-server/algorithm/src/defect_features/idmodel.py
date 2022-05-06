import numpy as np
import pandas as pd
from defect_features.config import conf

class Idmodel:

    def getdata(self, pythonProjectPath,project):
        data = pd.read_csv(pythonProjectPath+"/defect_features/report/"+project+'__'+conf.modelName+".csv")
        trainSet = np.array(data)+0
        trainSet = np.insert(trainSet, 0, np.ones(trainSet.shape[0]), axis=1)
        trainSet = trainSet[np.argsort(trainSet[:,-1])]
        train = trainSet[:, :-1]
        label = trainSet[:, -1]
        # print(trainArr)
        return train, label

    def sigmoid(self, z):
        return 1.0 / (1 + np.exp(-z))
        # s = 1 / (1 + np.exp(-z))
        # ds = s * (1 - s)
        # return ds

    def normalizeRows(self, x):
        x = np.array(x, dtype=np.float64)
        x_norm = np.linalg.norm(x, axis=1, keepdims=True)
        x = x / x_norm
        return x


    def gradDescent(self, train, label, lr, epoch):
        M, N = train.shape
        label = label.reshape(-1, 1)
        errlist = []
        beta = np.ones((N, 1))
        for t in range(epoch):
            # print(np.dot(train, beta))
            z = np.dot(train, beta)
            z = np.array(z, dtype=np.float64)
            # print(z.dtype.name)
            p = self.sigmoid(z)

            dBetaMat = -train * (label - p)


            # shape (1,n)
            dBeta = np.sum(dBetaMat, axis=0, keepdims=True, dtype=np.float64)
            # print(dBeta.dtype.name)
            beta -= lr * dBeta.T

            pre = self.predict(beta, train)
            errorRate = self.cntErrRate(pre, label)
            errlist.append(errorRate)
        return beta, errlist

    def predict(self, beta, trainArr):
        z = np.dot(trainArr, beta)
        z = np.array(z, dtype=np.float64)
        preArr = self.sigmoid(z)
        preArr[preArr > 0.5] = 1
        preArr[preArr <= 0.5] = 0

        return preArr


    def undersampling(self, train, label):
        clean_size = sum(label == 0)
        buggy_size = sum(label == 1)


        clean_data = train[0:clean_size, :]
        buggy_data = train[clean_size:clean_size+buggy_size, :]

        if (clean_size > buggy_size):
            row_rand_array = np.arange(clean_data.shape[0])
            np.random.shuffle(row_rand_array)
            sampled_data = clean_data[row_rand_array[0:buggy_size]]

            ret_data = np.vstack((sampled_data, buggy_data))
            ret_label = np.append(np.zeros(buggy_size), np.ones(buggy_size))
        else:
            row_rand_array = np.arange(buggy_data.shape[0])
            np.random.shuffle(row_rand_array)
            sampled_data = buggy_data[row_rand_array[0:clean_size]]

            ret_data = np.vstack((clean_data, sampled_data))
            ret_label = np.append(np.zeros(clean_size), np.ones(clean_size))


        return ret_data, ret_label

    def cntErrRate(self, prelabel, label):
        M = len(prelabel)
        if M == 0:
            return 0

        cnt = 0.0

        for i in range(M):
            if prelabel[i] != label[i]:
                cnt += 1.0
        return cnt / float(M)


    def buildIdmodel(self, pythonProjectPath, project):
        train, label = self.getdata(pythonProjectPath,project)
        train, label = self.undersampling(train, label)

        train = self.normalizeRows(train)

        lr = 0.001
        epoch = 500
        beta, err = self.gradDescent(train, label, lr, epoch)
        # print(err[-1])
        # print(beta)

        np.savetxt(pythonProjectPath+"/train/"+project+'_'+conf.modelName+"_learnedModel.txt", beta, fmt='%f', delimiter=',')

    def runIdmodel(self, pythonProjectPath,project,projectName,modelName):
        data = pd.read_csv(pythonProjectPath + "/defect_features/report/"+project+"_one.csv")
        beta = np.loadtxt(pythonProjectPath+"/train/"+projectName+'_'+modelName+"_learnedModel.txt", delimiter=',')
        testSet = np.array(data) + 0
        testSet = np.insert(testSet, 0, np.ones(testSet.shape[0]), axis=1)
        testSet = testSet[np.argsort(testSet[:, -1])]
        test = testSet[:, :-1]
        test = self.normalizeRows(test)
        # print(test)
        z = np.dot(test, beta)
        z = np.array(z, dtype=np.float64)
        # print(z)
        predict = self.sigmoid(z)
        # print(predict)
        predict[predict > 0.5] = 1
        predict[predict <= 0.5] = 0
        return predict


# if __name__ == '__main__':
    # run("/Users/lifeasarain/Desktop/JITO/JIT-Identification")
