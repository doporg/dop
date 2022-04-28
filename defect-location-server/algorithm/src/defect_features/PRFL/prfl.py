import math
import sys
import numpy
def testCoverageMatrix(testCoverage):
    file = open(testCoverage, "r",encoding='utf-8')
    row = file.readlines()
    file.close()

    list_text = []
    list_lable = []
    failednum=0
    passnum=0
    for line in row:
        s = []
        for i in line:
            if i== '1' or i== '0':
                s.append(float(i))
            if i=='+' :
                passnum=passnum+1
                list_lable.append(0)
            if i=='-' :
                failednum=failednum+1
                list_lable.append(1)
        list_text.append(s)

    return list_text,list_lable,failednum,passnum

def getTestPara(test_text,test_lable,failtest,passtest):
    paraMatrix=[]
    for j in range(len(test_text[0])):
        tempMatrix=[]
        Ef=0
        Ep=0
        for i in range(len(test_text)):
            if test_text[i][j]==1:
                if test_lable[i]==1:
                    Ef=Ef+1
                else:
                    Ep=Ep+1

        Nf=failtest-Ef
        Np=passtest-Ep

        tempMatrix.append(Ef)
        tempMatrix.append(Ep)
        tempMatrix.append(Nf)
        tempMatrix.append(Np)
        paraMatrix.append(tempMatrix)

    return paraMatrix
def getMethodMatrix(methInvoke):
    '''
    file = open(methInvoke, "r",encoding='utf-8')
    row = file.readlines()
    file.close()
    a=1
    methodMatrix=[]
    lineSum=[]
    for line in row:
        s = []
        sum=0
        for i in line:
            if(i=='x'):
                s.append(a)
                sum=sum+a
            elif(i.isdigit()):
                s.append(float(i))
                sum=sum+(float(i))
        methodMatrix.append(s)
        lineSum.append(sum)
    for i in range(len(methodMatrix)):
        if(lineSum[i]!=0):
            for j in range(len(methodMatrix)):
                methodMatrix[i][j]=float(format(methodMatrix[i][j]/lineSum[i],'.3f'))

    methodMatrix=numpy.transpose(methodMatrix)
    '''
    methodMatrix= [[0 for i in range(len(methInvoke[0]))] for i in range(len(methInvoke[0]))]
    return methodMatrix
def getFailTmMatrix(test_text,test_lable):
    Tm=[]
    for i in range(len(test_lable)):
        if test_lable[i]==1:
            tmOne=[]
            sum=0
            for j in test_text[i]:
                if(j==1):
                    sum=sum+1
            for m in test_text[i]:
                if(m==1):
                    tmOne.append(float((format(1/sum,'.3f'))))
                else:
                    tmOne.append(0)
            Tm.append(tmOne)
    return numpy.transpose(Tm)
def getPassTmMatrix(test_text,test_lable):
    Tm=[]
    for i in range(len(test_lable)):
        if test_lable[i]==0:
            tmOne=[]
            sum=0
            for j in test_text[i]:
                if(j==1):
                    sum=sum+1
            for m in test_text[i]:
                if(m==1):
                    tmOne.append(float((format(1/sum,'.3f'))))
                else:
                    tmOne.append(0)
            Tm.append(tmOne)
    return numpy.transpose(Tm)
def getFailMtMatrix(test_text,test_lable):
    Mt=[]
    for i in range(len(test_text[0])):
        Mtone=[]
        sum=0
        for j in range(len(test_text)):
            if(test_text[j][i]==1 and test_lable[j]==1):
                sum=sum+1
        for m in range(len(test_text)):
            if(test_lable[m]==1):
                if(test_text[m][i]==1):
                    Mtone.append(float(format(1/sum,'.3f')))
                else:
                    Mtone.append(0)
        Mt.append(Mtone)
    return numpy.transpose(Mt)
def getPassMtMatrix(test_text,test_lable):
    Mt=[]
    for i in range(len(test_text[0])):
        Mtone=[]
        sum=0
        for j in range(len(test_text)):
            if(test_text[j][i]==1 and test_lable[j]==0):
                sum=sum+1
        for m in range(len(test_text)):
            if(test_lable[m]==0):
                if(test_text[m][i]==1):
                    Mtone.append(float(format(1/sum,'.3f')))
                else:
                    Mtone.append(0)
        Mt.append(Mtone)
    return numpy.transpose(Mt)
def calFailV(test_text,test_lable):
    FailV=[]
    sum=0.0
    failTestCov=[]
    for i in range(len(test_lable)):
        if(test_lable[i]==1):
            time=0
            for j in test_text[i]:
                if(j==1):
                    time=time+1
            sum=sum+float(format(1/time,'.3f'))
            failTestCov.append(time)
    for i in failTestCov:
        failV=[]
        failV.append (float(format(float((format(1/i,'.3f')))/sum,'.3f')))
        FailV.append(failV)
    return FailV
def calPassV(test_lable):
    PassV=[]
    time=0
    for i in range(len(test_lable)):
        if(test_lable[i]==0):
                time=time+1
    for i in range(time):
        passv=[]
        passv.append (float( (format(1/time,'.3f') ) ))
        PassV.append(passv)
    return PassV
def cal(d,a,V,methodMatrix,TmMatrix,MtMatrix,times):
    n=len(methodMatrix)
    m=len(TmMatrix[0])
    Xm=[]
    Xt=[]
    for i in range (n):
        xm=[]
        xm.append(0)
        Xm.append(xm)
    for i in range(m):
        xt=[]
        xt.append(0)
        Xt.append(xt)
    Xm=numpy.matrix(Xm)
    print("Xm")
    print(Xm)
    Xt=numpy.matrix(Xt)
    print("Xt")
    print(Xt)
    V=numpy.matrix(V)
    print("V")
    print(V)
    methodMatrix=numpy.matrix(methodMatrix)
    print("methodMatrix")
    print(methodMatrix)
    TmMatrix=numpy.matrix(TmMatrix)
    print("TmMatrix")
    print(TmMatrix)
    MtMatrix=numpy.matrix(MtMatrix)
    print("MtMatrix")
    print(MtMatrix)

    for i in range (times):

        Ym=d*a*methodMatrix*Xm + d*TmMatrix*Xt
        Yt=d*MtMatrix*Xm +(1-d)*V
        if(numpy.max(Ym)!=0):
            Xm=Ym/numpy.max(Ym)
        if(numpy.max(Yt)!=0):
            Xt=Yt/numpy.max(Yt)
        '''
        print("第"+str(i+1)+"次")
        print("Ym",Ym)
        print("Yt",Yt)
        print("Xm",Xm)
        print("Xt",Xt)
        '''
    return  Xm


def calFinalPara(FailXm, PassXm, paraMatrix, test_lable, failtest, passtest,flag):
    for i in range(len(paraMatrix)):
        ef=FailXm[i]/numpy.max(FailXm)*failtest
        nf=failtest-ef
        if(flag):
            ep=PassXm[i]/numpy.max(PassXm)*passtest
            np=passtest-ep
        else:
            ep=0
            np=0
        paraMatrix[i][0]=float(ef)
        paraMatrix[i][1]=float(ep)
        paraMatrix[i][2]=float(nf)
        paraMatrix[i][3]=float(np)
    return numpy.matrix(paraMatrix)
def calSBFL(finalPara,method):
    result=[]
    print(finalPara)
    finalPara=finalPara.tolist()
    if(method=="Ochiai"):
        for i in range(len(finalPara)):
            result.append(float(format(( finalPara[i][0]/
                           (math.sqrt(
                               (finalPara[i][0]+finalPara[i][1])
                               * (finalPara[i][0]+finalPara[i][2])
                           ))
                           ),'.3f')))
    if(method=="Tarantula"):
        for i in range(len(finalPara)):
            result.append(float(format(
                                        ( (finalPara[i][0]/(finalPara[i][0]+finalPara[i][2]))/
                                          (
                                                  (finalPara[i][0]/(finalPara[i][0]+finalPara[i][2]))+
                                            (finalPara[i][1]/(finalPara[i][1]+finalPara[i][3]))
                                         )
                                         )
                ,'.3f')))
    if(method=="SBI"):
        for i in range(len(finalPara)):
            result.append(float(format(
                ( 1-(finalPara[i][1]/(finalPara[i][1]+finalPara[i][0]))
                  )
                ,'.3f')))
    if(method=="Jaccard"):
        for i in range(len(finalPara)):
            result.append(float(format(
                ( finalPara[i][1]/(finalPara[i][0]+finalPara[i][1]+finalPara[i][2])
                  )
                ,'.3f')))
    if(method=="Op2"):
        for i in range(len(finalPara)):
            result.append(float(format(
                ( finalPara[i][0]-(finalPara[i][1]/(1+finalPara[i][1]+finalPara[i][3]))
                  )
                ,'.3f')))
    return result
def sort(result):
    rank=[]
    for q in range(len(result)):
        qq=1
        for qqq in range(len(result)):
            if(result[qqq]>result[q]):
                qq=qq+1
        rank.append(qq)
    return rank
def finalArray(result,rank):
    finalarray=[]
    for ww in range(1,len(rank)+1):
        for www in range(len(rank)):
            if(rank[www]==ww):
                stri="可疑度第"+str(ww)+"的分数为："+str(result[www])+" 位置为："+str(www+1)
                finalarray.append(stri)
    return  finalarray
def writeToFile(finalarray,projectpath):
    file2 = open(projectpath+"/defect_features/PRFL/finalResult.txt", "w",encoding='utf-8')
    for line in finalarray:
        file2.write(str(line)+'\n')
    file2.close()

def locate(testCoverage,method,projectpath):
    test_text,test_lable,failtest,passtest=testCoverageMatrix(testCoverage)
    print("测试覆盖矩阵")
    for i in test_text:
        print(i)
    paraMatrix=getTestPara(test_text,test_lable,failtest,passtest)
    print("ef,ep,nf,np矩阵")
    for i in paraMatrix:
        print(i)
    methodMatrix=getMethodMatrix(test_text)

    FailTmMatrix=getFailTmMatrix(test_text,test_lable)
    PassTmMatrix=getPassTmMatrix(test_text,test_lable)

    FailMtMatrix=getFailMtMatrix(test_text,test_lable)
    PassMtMatrix=getPassMtMatrix(test_text,test_lable)

    FailV=calFailV(test_text,test_lable)
    PassV=calPassV(test_lable)

    d=0.7
    a=0.001
    times=25

    print("Fail:")
    FailXm=cal(d,a,FailV,methodMatrix,FailTmMatrix,FailMtMatrix,times)
    flag=True
    if(passtest>0):
        print(">0 success:")
        PassXm=cal(d,a,PassV,methodMatrix,PassTmMatrix,PassMtMatrix,times)
    else:
        print("=0 success:")
        PassXm=0
        flag=False
    print("final:")
    finalPara=calFinalPara(FailXm,PassXm,paraMatrix,test_lable,failtest,passtest,flag)
    result=calSBFL(finalPara,method)
    print("可疑度为:",result)
    rank=sort(result)
    print("排名为：   ",rank)
    finalarray=finalArray(result,rank)
    print("结果是：  ",finalarray)
    writeToFile(finalarray,projectpath)
    fopen = open(projectpath+"/defect_features/PRFL/finalResult.txt", "r",encoding='utf-8')
    lines = fopen.readlines()
    return lines
if __name__ == '__main__':
    locate(sys.argv[1],sys.argv[2],sys.argv[3])
    #locate("D:/JITO/JITO-Identification/defect_features/PRFL/testCoverage.txt","Ochiai","D:/JITO/JITO-Identification")