'''
Created on Oct 4, 2009

@author: jnelson
'''

class Program(object):
    '''
    classdocs
    '''

    PROGRAM = 0
    DECLARATIONS = 1
    DECLARATION = 2
    STATEMENTS = 2
    STATEMENT = 3
    EXPRESSION = 4
    UNARY_OPERATOR = 5
    BINARY_OPERATOR = 6

    def __init__(self, file_location):
        '''
        Constructor
        '''
        self.file_loc = file_location
        self.current_state = 0

    def parse_file(self):
        pass
        
