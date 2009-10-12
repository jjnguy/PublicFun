'''
Created on Oct 6, 2009

@author: jnelson
This file contains definitions for methods that parse a Program
'''
#This is the regular expression module
import re

#Token classes
KEYWORD = 0
IDENTIFIER = 1
CONSTANT = 2
COMMENT = 3
OPERATOR = 4
STRUCTURE_ELEMENT = 5 #{}();
ASSIGNMENT = 6

constant_re = re.compile("-?\\d+$")
keyword_re = re.compile("(variable|if|while|println)$")
#matches a letter and 0-31 letters or numbers
identifier_re = re.compile("[A-Za-z]([A-Za-z0-9]{0,31})$")
comment_re = re.compile("//(.*|.*\\n)$")
operator_re = re.compile("(\\+|-|\\*|/|&&|\\|\\||==|!=|<|<=|>|>=)$")
whitespace_re = re.compile("\\s+$")
structure_elm_re = re.compile("[\\(\\){};]$")
assignment_re = re.compile("=$")
anytoken_re = re.compile("(" + constant_re.pattern + "|" + keyword_re.pattern + "|" + identifier_re.pattern + "|" + comment_re.pattern + "|" + operator_re.pattern + "|" + structure_elm_re.pattern + "|" + assignment_re.pattern +")")
legal_chars = re.compile("([0-9A-Za-z]|\\+|-|\\*|/|&|\\||=|!|>|<|\\n|\\(|\\)|;|{|}|,|\\s)$");

def tokenize(file_loc):
    '''
    Splits a file into tokens...yay!!
    
    '''
    file = open(file_loc, "r")
    tokens = list()

    line_number = 1 #keep track of current line number for better error reporting
    #read each character in the program one-by-one
    token = ""
    for char in file.read():
        if not legal_chars.match(char):
            raise Exception(str.format("Error while tokenizing. Character '{}' was an illegal character on line {}.", char, line_number))
        if char == "\n": 
            line_number += 1
        token += char
        if anytoken_re.match(token): #if we have a valid token match, we keep reading chars to see if the token continues
            continue
        #If we got here, that means we currently have a token that was made invalid by the most recent char
        #we now need to add the valid part of the token and continue building up more tokens
        _add_token(token[0: len(token) - 1], tokens, line_number);
        token = token[len(token) - 1]
    if token != "": #if there is still a token in the buffer, we add it
        _add_token(token, tokens, line_number)
    return tokens

def _add_token(token, tokens, line_number):
    if whitespace_re.match(token):
        return
    if keyword_re.match(token):
        tokens.append((token, KEYWORD))
    elif constant_re.match(token):
        tokens.append((token, CONSTANT))
    elif comment_re.match(token):
        tokens.append((token, COMMENT))
    elif identifier_re.match(token):
        tokens.append((token, IDENTIFIER))
    elif structure_elm_re.match(token):
        tokens.append((token, STRUCTURE_ELEMENT))
    elif operator_re.match(token):
        tokens.append((token, OPERATOR))
    elif assignment_re.match(token):
        tokens.append((token, ASSIGNMENT))
    else: #we may have a comment, or a bad token...we will see in a sec
        loc = token.find("//")
        if loc != -1:
            _add_token(token[0: loc], tokens, line_number)
            _add_token(token[loc: len(token)], tokens, line_number)
        else:
            raise Exception(str.format("Error while tokenizing. String '{}' was an illegal sequence of characters on line {}.", token, line_number))

if __name__ == "__main__":
    tokens = tokenize("programs/program")
    print(tokens)





