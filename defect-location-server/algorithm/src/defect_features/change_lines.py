import os
import subprocess
import json
from defect_features.config import conf

def wrapper_change_path(func):
    cwd = os.getcwd()

    def inner(*args, **kwargs):
        return func(*args, **kwargs)


    os.chdir(cwd)
    return inner

class Changelines:
    @wrapper_change_path
    def getChangelines(self, project):
        target_path = conf.project_path(project)
        os.chdir(target_path)
        #cmd = r"git diff --unified=0 --diff-filter=M HEAD~1 HEAD  | grep -v -e '^[+-]' -e '^index'"
        cmd = r"git diff --unified=0 --diff-filter=M HEAD~1 HEAD  "
        lines_path = conf.local_path+"train/"+project+"diff.txt"
        print(lines_path)
        out = subprocess.check_output(cmd, shell=True).decode('utf-8', errors='ignore')
        #out = subprocess.check_output(cmd).decode('utf-8', errors='ignore')
        print(out)
        with open(lines_path, 'w',encoding='utf-8' ) as f_obj2:
            f_obj2.write(out)
        fopen = open(conf.local_path+"train/"+project+"diff.txt", 'r',encoding='utf-8')
        lines = fopen.readlines()
        # for line in lines:
        # file_lines = {}
        file_lines = []
        line_numbers = []
        javafile = []

        for index in range(len(lines)):

            if ('diff' == lines[index][0:4]):
                related_path = lines[index][12:].split(" ")[0]
                # print(related_path)
                line_numbers = []
            if('@@' == lines[index][0:2]):
                projectfile = open(target_path+related_path, 'r',encoding='utf-8')
                java = projectfile.readlines()
                # print(target_path+related_path)

                number = lines[index].split("+")[1].split(" @@")[0]
                start = number.split(',')[0]
                if(',' in number):
                    end = int(number.split(',')[1]) + int(start) - 1
                    for i in range(int(start) - 1, end):
                        line_numbers.append(i)
                        fl_dict = {}
                        fl_dict[target_path + related_path] = i
                        file_lines.append(fl_dict)
                        javafile.append(java[i-1])
                else:
                    line_numbers.append(int(start) - 1)
                    fl_dict = {}
                    fl_dict[target_path + related_path] = int(start) - 1
                    file_lines.append(fl_dict)
                    javafile.append(java[int(start) - 1])
                # file_lines[target_path+related_path] = line_numbers


                # print(start)
                # print(end)


        # print(file_lines)
        json_str = json.dumps(file_lines)
        with open(conf.local_path+"train/"+project+"test.txt", 'w', encoding='utf-8') as fl:
            fl.writelines(json_str)

        with open(conf.local_path+"train/"+project+"javafile.java", 'w', encoding='utf-8') as jf:
            jf.writelines(javafile)

        return  lines

        # json_str = json.dumps(file_lines)
        # with open('/Users/lifeasarain/Desktop/tmp/JITO/JITO-Identification/train/test.txt', 'w', ) as fl:
        #     fl.write(json_str)

# if __name__ == '__main__':
#     run()
