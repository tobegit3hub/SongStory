# pack/archive files into a zip file (PKZIP format)
# test it, show information, then unpack the zip file
# tested with Python24    vegaseat    21jan2007

import zipfile

str1 = "We always hold hands. If I let go, she shops."
str2 = "I take my wife everywhere, but she keeps finding her way back."
str3 = "She got a mudpack, looked good for a while, then the mud fell off."

# write test file 1
fout = open("ZipTest1.txt", "w")
fout.write(str1)
fout.close()

# write test file 2
fout = open("ZipTest2.txt", "w")
fout.write(str2)
fout.close()

# write test file 3
fout = open("ZipTest3.txt", "w")
fout.write(str3)
fout.close()

archive_list = ["ZipTest1.txt", "ZipTest2.txt", "ZipTest3.txt"]

# save the files in the archive_list into a PKZIP format .zip file
zfilename = "Wife101.zip"
zout = zipfile.ZipFile(zfilename, "w")
for fname in archive_list:
    zout.write(fname)
zout.close()

print '-'*40

# test if the file is a valid pkzip file
if zipfile.is_zipfile(zfilename):
    print "%s is a valid pkzip file" % zfilename
else:
    print "%s is not a valid pkzip file" % zfilename

print '-'*40

# open the zipped file
zfile = zipfile.ZipFile( zfilename, "r" )

# retrieve information about the zip file
zfile.printdir()

print '-'*40

# get each archived file and process the decompressed data
for info in zfile.infolist():
    fname = info.filename
    # decompress each file's data
    data = zfile.read(fname)
    
    # testing --> display file's contents if a text file
    if fname.endswith(".txt"):
        print "These are the contents of %s:" % fname
        print data
    
    # save the decompressed data to a new file
    filename = 'unzipped_' + fname
    fout = open(filename, "w")
    fout.write(data)
    fout.close()
    print "New file created --> %s" % filename
    print '-'*40
