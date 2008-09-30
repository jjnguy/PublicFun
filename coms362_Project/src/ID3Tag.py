import struct

class ID3v2_3Tag:
    def __init__(self, mp3_filename):
        self.file = open(mp3_filename, "rb")
        # create the header using the first ten bytes in the file
        self.headder = ID3v2_3TagHeader(self.file.read(10))
        self.headder.printIt()
        self.headder.noFlags()
        self.frames = []
        bytes_left = self.headder.len
        
        
        #while(bytes_left):
        if (self.headder.major_version < 3):
            while bytes_left:
                fr_head = ID3v2_2FrameHeader(self.file.read(6))
                bytes_left-=6
                frame = ID3v2_2Frame
        else:
            fr_head = ID3v2_3FrameHeader(self.file.read(10))
        fr_head.pritnIt()
        

class ID3v2_3TagHeader:
    def __init__(self, ten_byte_string):
        self.whole_string = ten_byte_string
        self.file_identifier, self.major_version, self.minor_version, self.flags, self.len \
 = struct.unpack('>3sBBBI', self.whole_string)
        if (self.file_identifier != "ID3"):
            print "FUCK!"
        if (not self.noFlags()):
            print "I will probably fail soon"
        if(self.major_version < 3):
            print "great, now we are in compatibility mode"
    
    def noFlags(self):
        if (self.flags != 0):
            print "reading this may fail"
            return False
        return True
        
        
    def printIt(self):
        print "ID: %s" % self.file_identifier
        print "Version: %s.%s" % (self.major_version, self.minor_version)
        print "Flags: %s" % self.flags
        print "Len: %s" % self.len

class ID3v2_2Frame:
    def __init__(self):
        print "stuf v2"

class ID3v2_2FrameHeader:
    def __init__(self, ten_byte_string):
        self.frame_id, b1, b2, b3 = struct.unpack(">3sBBB", ten_byte_string)
        self.frame_len = (b1 << 8) + (b2 << 4) + b3
        
    def pritnIt(self):
        print self.frame_id
        print self.frame_len
        
        
class ID3v2_3Frame:
    def __init__(self, headder):
        print "stuff"

class ID3v2_3FrameHeader:
    def __init__(self, ten_byte_string):
        self.frame_id, self.frame_len, self.status_mes, self.format_desc = struct.unpack(">4sIBB", ten_byte_string)
        
    def pritnIt(self):
        print self.frame_id
        print self.frame_len
        print self.status_mes
        print self.format_desc
        

tag = ID3v2_3Tag("12 Sex Rap.mp3")