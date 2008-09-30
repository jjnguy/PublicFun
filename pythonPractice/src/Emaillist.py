
filename = raw_input("Filename Please: ")
file_input = open(filename)
file_output = open(filename + ".emails", "w")
for line in file_input:
    file_output.write("%s@iastate.edu,\n" % line[:len(line)-1])
