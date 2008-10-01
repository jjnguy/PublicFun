import struct

class ID3v2_3Tag:
    def __init__(self, mp3_filename):
        self.file = open(mp3_filename, "rb")
        # create the header using the first ten bytes in the file
        self.header = ID3v2_3TagHeader(self.file.read(10))
        self.header.printIt()
        self.header.noFlags()
        self.frames = []
        bytes_left = self.header.len
        
        #legacy support
        if self.header.major_version < 3:
            while bytes_left > 0:
                fr_head = ID3v2_2FrameHeader(self.file.read(6))
                fr_head.pritnIt()
                bytes_left-=6
                frame = ID3v2_2Frame(fr_head, self.file.read(fr_head.frame_len))
                frame.printIt()
                bytes_left-=fr_head.frame_len
                print "Bytes left in the tag: %i" % bytes_left
        else:
            print "take up space"
            # TODO support the current standard
        

class ID3v2_3TagHeader:
    def __init__(self, ten_byte_string):
        self.whole_string = ten_byte_string
        self.file_identifier, self.major_version, self.minor_version, self.flags, b1, b2, b3, b4 = struct.unpack('>3sBBBBBBB', self.whole_string)
        #the tag size encoding is lame!
        self.len = (b1 << 21) + (b2 << 14) + (b3 << 7) + b4
        
        if (self.file_identifier != "ID3"):
            print "FUCK!"
        if (not self.noFlags()):
            print "I will probably fail soon"
        if(self.major_version < 3):
            print "great, now we are in compatibility mode"
    
    def noFlags(self):
        if (self.flags != 0):
            return False
        return True
        
    def printIt(self):
        print "ID: %s" % self.file_identifier
        print "Version: %s.%s" % (self.major_version, self.minor_version)
        print "Flags: %s" % self.flags
        print "Len: %s" % self.len

class ID3v2_2Frame:
    def __init__(self, header, byte_string):
        if header.frame_id[0] == "T":
            self.data = struct.unpack("%is" % header.frame_len, byte_string)[0].strip()
        elif header.frame_id == "COM":
            self.language, self.data = struct.unpack(">3s%is" % (header.frame_len - 4), byte_string[1:])
            print self.language
        elif header.frame_id == "UFI":
            self.data = struct.unpack("%is" % header.frame_len, byte_string)
        else:
            self.data = "Complex Shit"
        
    def frameName(self):
        return header.frame_id
    
    def printIt(self):
        print self.data

class ID3v2_2FrameHeader:
    def __init__(self, six_byte_string):
        self.frame_id, b1, b2, b3 = struct.unpack(">3sBBB", six_byte_string)
        #building ints from 3 bytes is lame
        self.frame_len = (b1 << 16) + (b2 << 8) + b3
        
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