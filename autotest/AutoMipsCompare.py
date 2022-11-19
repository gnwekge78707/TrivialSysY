import os

DEV_NULL = 'log.txt'
ANS_FILE = 'TestProgram'
JAR_NAME = 'dev0.5.jar'

MARS_NAME = 'Mars_2021.jar'
FORMAT_INPUT = 'input.txt'
ADD_HEAD = '''
#include <stdio.h>

int getint() {
    int ret;
    scanf("%d", &ret);
    return ret;
}
'''

fileDict = []
inputDict = {}

def get_files():
    print(os.path)
    for filepath, dirnames, filenames in os.walk(os.getcwd()):
        for filename in filenames:
            spl = filename.split('.')
            if len(spl) == 2 and spl[1] == 'txt' and spl[0].startswith('testfile'):
                inputname = "input"
                inputname += spl[0][8:] + ".txt"
                fileDict.append((filepath, filename, inputname))
                print(os.path.join(filepath, filename))
                print(os.path.join(filepath, inputname))


if __name__ == '__main__':
    diff = []
    fileId = []
    get_files()

    for i, fileInfo in enumerate(fileDict):
        print('---------------- running ' + 'testfile' + str(i) + ' ----------------')
        if os.path.exists('testfile.txt'):
            os.remove('testfile.txt')
        if os.path.exists('input.txt'):
            os.remove('input.txt')
        input_filename = os.path.join(fileInfo[0], fileInfo[1])
        inputname = os.path.join(fileInfo[0], fileInfo[2])
        print(input_filename)
        os.rename(input_filename, 'testfile.txt')

        with open(inputname, 'r', errors='ignore') as file:
            input_cont = file.read()
        with open('input.txt', 'w') as file:
            bb = ""
            for ii in input_cont:
                if ii == ' ':
                    bb = bb + '\n'
                else:
                    bb = bb + ii
            file.write(bb)

        # os.rename(inputname, 'input.txt')
        print('---> init finished :', input_filename)
        ######################################################################## compiler run
        os.system('java -jar ' + JAR_NAME + ' > ' + DEV_NULL)
        name1 = 'output_' + JAR_NAME.split('.')[0] + '_' + str(i) + '.txt'
        if os.path.exists(name1):
            os.remove(name1)
        print('---> ' + JAR_NAME + ' finished')
        ######################################################################## gcc ans run
        with open('testfile.txt', 'r', errors='ignore') as file:
            code = file.read()
        with open(ANS_FILE + '.cpp', 'w') as file:
            file.write(ADD_HEAD + '\n' + code)
        os.system('g++ ' + ANS_FILE + '.cpp -o ' + ANS_FILE + ' -std=c++11')
        os.system('.\\' + ANS_FILE + ' < ' + FORMAT_INPUT + ' > answer.txt')
        print('---> run ' + ANS_FILE + '.cpp finished')
        ######################################################################## mars run mips
        os.system('java -jar ' + MARS_NAME + ' mips.txt < ' + FORMAT_INPUT + ' > myanswer.txt')
        with open("myanswer.txt", 'r') as file:
            text = file.read()
        text = text.split('\n')[2:]
        with open("myanswer.txt", 'w') as file:
            file.write('\n'.join(text))
        print('---> simulate ObjectCode' + str(i) + ' finished')
        ########################################################################
        with open('answer.txt', 'r') as file1:
            cont1 = file1.read()
        with open("myanswer.txt", 'r') as file2:
            cont2 = file2.read()
        os.rename('testfile.txt', input_filename)
        #os.rename('input.txt', inputname)
        if cont1 != cont2:
            diff.append('testfile' + str(i) + '.txt')
        print('---> compare finished, ' + str(cont1 == cont2))
        print('')
    #os.remove('log.txt')
    if len(diff) == 0:
        print('^^^ all same ^^^')
        os.remove(DEV_NULL)
        os.remove(FORMAT_INPUT)
        os.remove(ANS_FILE + '.c')
        os.remove(ANS_FILE + '.exe')
        for i in os.listdir():
            spl = i.split('.')
            if len(spl) == 2 and spl[1] == 'txt' and spl[0].startswith('output'):
                try:
                    name_remove = i
                    os.remove(name_remove)
                except:
                    pass
    else:
        print('diff at ' + str(diff))
