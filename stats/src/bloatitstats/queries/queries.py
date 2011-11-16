
import sys
import httplib

class queries(object):
    def __init__(self, cursor, output, refdate = 'now'):
        super(queries, self).__init__()
        self.cursor = cursor
        self.output = output
        self.refdate = refdate
        self.cursor.execute("create table IF NOT EXISTS mydates (time datetime)")
        self.cursor.execute('''select date(?)''', (refdate,))
        if self.cursor.fetchone()[0] == None:
            raise Exception, "Wrong date format (%s) !" % refdate
        
    def _print_result(self):
        for row in self.cursor:
            print row

    def _double_array_serialize(self, afile, array, varname):
        afile.write("var %s = [ " % varname)
        for row in array:
            afile.write("[")
            for cell in row:
                afile.write(str(cell))
                afile.write(",")
            afile.write("],")
        afile.write("];")

    def _array_serialize(self, afile, array, varname):
        afile.write("var %s = [ " % varname)
        for row in array:
                afile.write(str(row))
                afile.write(",")
        afile.write("];")

