import sys

import pandas as pd
import numpy as np
import math


def corpus(locationprojectname,pythonpath,projectname,modelName):
    #得到语料库
    path=pythonpath+"/defect_features/utils/data_tmp/"+projectname+'__'+modelName+"_a.csv"
    data = pd.read_csv(path)
    data = data["clean_lines"]
    data = np.array(data)
    data = list(data)
    data_list = []
    for i in range(len(data)):
        if (isinstance(data[i], str) and len(data[i]) >= 3):
            s = data[i][2:len(data[i]) - 2].split("\", \"")
            for k in range(len(s)):
                t = s[k].strip()
                t = t.replace('\\', '')
                a = t.find("\"")
                b = t.rfind("\"")
                if (a != -1 and b != -1):
                    if (a != b):
                        t = t[0:a] + t[b + 1:len(t)]
                    else:
                        t = ""
                if (t.find("//") != -1):
                    c = t.find("//")
                    t = t[0:c]
                if (len(t) >= 10):
                    data_list.append(t)

    file2 = open(pythonpath+"/train/"+projectname+"locationTrain.txt", "w", encoding='utf-8')
    for line in data_list:
        file2.write(str(line) + '\n')
    file2.close()
    #进行建模
    fencilist, fencinum = fenci(data_list)
    zidiandata = zidian(fencilist)
    #进行判断
    testdata(pythonpath+"/train/"+locationprojectname+"javafile.java", dictdata=zidiandata, totalnum=fencinum,pythonpath=pythonpath,projectname=locationprojectname)
    print("locate sucess")
    fopen = open(pythonpath+"/train/"+locationprojectname+"locationresult.csv", "r",encoding='utf-8')
    lines = fopen.readlines()

    return lines


def fenci(data):
    finallist = []
    fencinum = 0
    for te in data:
        temp = []
        left = 0
        right = 0
        while (left < len(te)):
            if (te[left].isalpha()):
                right = left + 1
                while (right < len(te) and (te[right].isalpha() or te[right].isdigit() or te[right] == '_')):
                    right = right + 1
                a = te[left:right]
                left = right
                temp.append(a)
                fencinum = fencinum + 1
            elif (te[left].isdigit()):
                right = left + 1
                while (right < len(te) and te[right].isdigit()):
                    right = right + 1
                a = te[left:right]
                left = right
                temp.append(a)
                fencinum = fencinum + 1
            elif (te[left] == ',' or te[left] == '.' or te[left] == '_' or te[left] == '(' or te[left] == ')'
                  or te[left] == '{' or te[left] == '}' or te[left] == '=' or te[left] == '[' or te[left] == ']'
                  or te[left] == '>' or te[left] == '<' or te[left] == '+' or te[left] == '-' or te[left] == '*'
                  or te[left] == '/' or te[left] == '!' or te[left] == '@' or te[left] == '#' or te[left] == '%'
                  or te[left] == '$' or te[left] == '|' or te[left] == ';' or te[left] == ':' or te[left] == '?'
            ):
                temp.append(te[left])
                fencinum = fencinum + 1
                left = left + 1
            else:
                left = left + 1

        finallist.append(temp)

    return finallist, fencinum


def zidian(data):
    dictdata = dict()
    for i in data:
        # one
        for j in i:
            if (dictdata.get(j) == None):
                dictdata[j] = 1
            else:
                time = dictdata.get(j)
                dictdata[j] = time + 1
        # two
        for j in range(1, len(i)):
            strtemp = i[j - 1] + i[j]
            if (dictdata.get(strtemp) == None):
                dictdata[strtemp] = 1
            else:
                time = dictdata.get(strtemp)
                dictdata[strtemp] = time + 1
        # three
        for j in range(2, len(i)):
            strtemp = i[j - 2] + i[j - 1] + i[j]
            if (dictdata.get(strtemp) == None):
                dictdata[strtemp] = 1
            else:
                time = dictdata.get(strtemp)
                dictdata[strtemp] = time + 1
        # four
        for j in range(3, len(i)):
            strtemp = i[j - 3] + i[j - 2] + i[j - 1] + i[j]
            if (dictdata.get(strtemp) == None):
                dictdata[strtemp] = 1
            else:
                time = dictdata.get(strtemp)
                dictdata[strtemp] = time + 1
        # five
        for j in range(4, len(i)):
            strtemp = i[j - 4] + i[j - 3] + i[j - 2] + i[j - 1] + i[j]
            if (dictdata.get(strtemp) == None):
                dictdata[strtemp] = 1
            else:
                time = dictdata.get(strtemp)
                dictdata[strtemp] = time + 1

    return dictdata


def testdata(path, dictdata, totalnum,pythonpath,projectname):
    tdata = []
    tdata2 = []
    lam = 0.5
    with open(path) as f1:
        for content in f1:
            tdata.append(content)

    with open(pythonpath+"/train/"+projectname+"test.txt",encoding='utf-8') as file2:
        content = file2.read()
    content=content[2:len(content)-2]
    textcontent=content.split('}, {')
    print(textcontent)

    for i in range(len(tdata)):
        tdata[i] = tdata[i].strip()
        #if (isinstance(tdata[i], str) and len(tdata[i]) >= 3):
        a = tdata[i].find("\"")
        b = tdata[i].rfind("\"")
        if (a != -1 and b != -1):
            if (a != b):
                tdata[i] = tdata[i][0:a] + tdata[i][b + 1:len(tdata[i])]

        tdata2.append(tdata[i])

    print(tdata2)

    fencidata, nu = fenci(tdata2)
    print(fencidata)

    # yuci
    probrow = []
    for da in fencidata:
        #print(da)
        probtoken = 0.0
        isnon = isnone(da, dictdata)
        if not isnon:
            if(len(da)>0):
                for i in range(len(da)):
                    p = proba(da=da, i=i, totalnum=totalnum, dictdata=dictdata, lam=lam)
                    p=math.log(p,2)
                    probtoken=probtoken+p
                rowprodata=-probtoken/len(da)
            else:
                rowprodata=-2
            probrow.append(rowprodata)
        else:
            probrow.append(-1)
    print(probrow)


    sort=[]
    for i in range(len(probrow)):
        nub=1
        if(probrow[i]==-1):
            sort.append(nub)
        else:
            for ii in range(len(probrow)):
                if(probrow[ii]>probrow[i] or probrow[ii]==-1):
                    nub=nub+1
            sort.append(nub)

    print(sort)
    sttarray=[]
    for i in range(1,len(sort)+1):
        for x in range(len(sort)):
            if(sort[x]==i):
                stt="可能性第"+str(i)+"的代码为："+tdata2[x]+"----修改所在位置: "+textcontent[x]
                sttarray.append(stt)


    file4 = open(pythonpath+"/train/"+projectname+"locationresult.csv", "w",encoding='utf-8')
    for line in sttarray:
        file4.write(str(line)+'\n')
    file4.close()



def proba(da, i, totalnum, dictdata, lam):
    if i == 0:
        return onej(da,i,totalnum,dictdata)
    if i == 1:
        return lam*twoj(da,i,dictdata) + (1-lam)*onej(da,i,totalnum,dictdata)
    if i == 2:
        return lam*threej(da,i,dictdata)+lam*(1-lam)*twoj(da,i,dictdata) + lam*(1-lam)*onej(da,i,totalnum,dictdata)
    if i == 3:
        return lam*fourj(da,i,dictdata)+lam*(1-lam)*threej(da,i,dictdata)+lam*lam*(1-lam)*twoj(da,i,dictdata) \
               + lam*lam*(1-lam)*onej(da,i,totalnum,dictdata)
    if i == 4:
        return lam*fivej(da,i,dictdata)+lam*(1-lam)*fourj(da,i,dictdata)+lam*(1-lam)*(1-lam)*threej(da,i,dictdata)\
               +lam*lam*lam*(1-lam)*twoj(da,i,dictdata) + lam*lam*lam*(1-lam)*onej(da,i,totalnum,dictdata)
    else:
        return lam*sixj(da,i,dictdata)+lam*lam*fivej(da,i,dictdata)+lam*lam*(1-lam)*fourj(da,i,dictdata)\
               +lam*lam*(1-lam)*(1-lam)*threej(da,i,dictdata) \
               +lam*lam*lam*lam*(1-lam)*twoj(da,i,dictdata) + lam*lam*lam*lam*(1-lam)*onej(da,i,totalnum,dictdata)

def sixj(da, i, dictda):
    s1=da[i-5]+da[i-4]+da[i-3]+da[i-2]+da[i-1]+da[i]
    s2=da[i-5]+da[i-4]+da[i-3]+da[i-2]+da[i-1]
    if(dictda.get(s2) is None):
        return 0
    return nonzero(dictda.get(s1))/dictda.get(s2)

def fivej(da, i, dictda):
    s1=da[i-4]+da[i-3]+da[i-2]+da[i-1]+da[i]
    s2=da[i-4]+da[i-3]+da[i-2]+da[i-1]
    if(dictda.get(s2) is None):
        return 0
    return nonzero(dictda.get(s1))/dictda.get(s2)

def fourj(da, i, dictda):
    s1=da[i-3]+da[i-2]+da[i-1]+da[i]
    s2=da[i-3]+da[i-2]+da[i-1]
    if(dictda.get(s2) is None):
        return 0
    return nonzero(dictda.get(s1))/dictda.get(s2)

def threej(da, i, dictda):
    s1=da[i-2]+da[i-1]+da[i]
    s2=da[i-2]+da[i-1]
    if(dictda.get(s2) is None):
        return 0
    return nonzero(dictda.get(s1))/dictda.get(s2)

def twoj(da, i, dictda):
    s1=da[i-1]+da[i]
    s2=da[i-1]
    return nonzero(dictda.get(s1))/dictda.get(s2)


def onej(data, indx, total, dictda):
    return dictda.get(data[indx]) / total


def nonzero(data):
    if data is None:
        return 0
    else:
        return data


def isnone(da, dictdata):
    for i in da:
        if dictdata.get(i) is None:
            return True
    return False


if __name__ == '__main__':
    corpus(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
    #corpus("Feeder","D:/JITO/JITO-Identification","druid","A")

