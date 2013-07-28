#!/usr/bin/env python
#coding=utf-8

import sys
import os
import zipfile

def main:
    number = sys.argv[1]

    songFileName = number+".mp3"
    lyricsFileName = number+".lrc"
    albumImageFileName = number+"_album.jpg"
    imageFileName0 = number+"_0.jpg"
    imageFileName1 = number+"_1.jpg"
    imageFileName2 = number+"_2.jpg"
    jsonFileName = number+".json"
    
    os.rename("model.json", jsonFileName)
    
    archiveFileNames = [songFileName, lyricsFileName, albumImageFileName, imageFileName0, imageFileName1, imageFileName2, jsonFileName]
    
    #print archiveFileNames
    
    outputFileName = number+".ss"
    zout = zipfile.ZipFile(outputFileName, "w")
    
    for fileName in archiveFileNames:
        zout.write(fileName)
    zout.close()

if __name__ == "__main__":
    main()


