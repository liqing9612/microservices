import os, time, sys

def ensure_dir(directory):
    if not os.path.exists(directory):
        os.makedirs(directory)

def get_currentscript_path():
    return os.path.dirname(os.path.realpath(sys.argv[0]))

def mv(filename, from_dir, to_dir):
    os.rename(from_dir + "/" + filename, to_dir + "/" + filename)

data_dir = get_currentscript_path() + "/../../data"
input_dir = data_dir + "/ready-to-analyze"
output_dir = data_dir + "/analyzed"

ensure_dir(input_dir)
ensure_dir(output_dir)



def analyse_file(input_dir, f):
    print "analyzing ", input_dir + "/" + f

while 1:
    for f in os.listdir (input_dir):
        analyse_file(input_dir, f)
        mv(f, input_dir, output_dir)
    time.sleep (5)

