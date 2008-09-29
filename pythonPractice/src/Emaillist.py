
filename = raw_input("Filename Please: ")
filein = open(filename)
fileout = open(filename + ".emails", "w")
for line in filein:
    fileout.write("%s@iastate.edu\n" % line[:len(line)-1])
filein.close()
fileout.close()
