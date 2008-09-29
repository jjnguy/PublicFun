import struct

class ID3V2Tag:
    
    def __init__(self, mp3_filename):
        self.file = open(mp3_filename, "rb")
        self.headder = ID3V2Headder(self.file.read(10))
        self.headder.printIt()
        

class ID3V2Headder:
    
    def __init__(self, ten_byte_string):
        self.whole_string = ten_byte_string
        self.file_identifier = self.whole_string[:3]
        self.major_version = struct.unpack('>b', self.whole_string[3:4])[0]
        self.minor_version = struct.unpack('>b', self.whole_string[4:5])[0]
        self.flags = self.whole_string[5:6]
        self.len = self.whole_string[6:10]
    
    def printIt(self):
        print "ID: %s" % self.file_identifier
        print "Version: %s.%s" % (self.major_version, self.minor_version)
        print "Flags: %s" % self.flags
        print "Len: %s" % self.len
        
tag = ID3V2Tag("10_Pet.mp3")