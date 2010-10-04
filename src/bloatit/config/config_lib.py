# -*- coding: utf-8 -*-

# Copyright (C) 2010 BloatIt.
# Copyright (C) 2009-2010 Matthieu Bizien.
#
# This file is part of BloatIt.
#
# BloatIt is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# BloatIt is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with BloatIt. If not, see <http://www.gnu.org/licenses/>.

"""Library used to create easily configuration"""

import logging
import os
import sys
from configparser import ConfigParser

#config_lib must not be dependant of perroquetlib
defaultLoggingHandler = logging.StreamHandler(sys.stdout)
defaultLoggingHandler.setFormatter(logging.Formatter("%(asctime)s.%(msecs)d-[%(name)s::%(levelname)s] %(message)s", "%a %H:%M:%S"))
defaultLoggingLevel = logging.DEBUG


class Parser(ConfigParser):
    """A general class to make parsers"""
    str2object = {
        "int": int,
        "string": lambda x: x,
        "stringlist": lambda s: s.split("\n"),
        "intlist": lambda s: [int(x) for x in s.split("\n")],
        "stringlistlist": lambda ll: [l.split("::") for l in ll.split("\n")]
        , }
    object2str = {
        "int": str,
        "string": lambda x: x,
        "stringlist": lambda l: ("\n").join(l),
        "intlist": lambda l: ("\n").join(str(i) for i in l),
        "stringlistlist": lambda ll: "\n".join("::".join(l) for l in ll)}

    def __init__(self):
        ConfigParser.__init__(self)

    def get_options(self):
        "return {option1: section1, ...}"
        return dict([(option, section)
                    for section in self.sections()
                    for option in self.options(section)])

    def set(self, section, option, value):
        "set an option."
        string = self.object2str[section](value)
        ConfigParser.set(self, section, option, string)

    def get(self, section, option):
        "get an option value for a given section."
        return self.str2object[section](ConfigParser.get(self, section, option))

    def items(self, section):
        """Return a list of tuples with (name, value) for each option
        in the section."""
        return [(option, self.get(section, option)) for option in self.options(section)]

    def __str__(self):
        return "<Parser "+"\n".join(section+"->"+option+": "+value
            for section in self.sections()
            for option, value in ConfigParser.items(self, section))+">"

class WritableParser(Parser):
    """A class that deal with writable parsers"""
    def __init__(self, path):
        Parser.__init__(self)
        self.path = path
        self.read(self.path)
        self._options = Parser.get_options(self)

    def save(self):
        "Only save the options that have changed"
        def mkpath(path):
            "Create the directeries components of a path"
            path2 = os.path.dirname(path)
            if not os.path.isdir(path2):
                mkpath(path2)
                os.mkdir(path2)
        mkpath(self.path)
        self.write(open(self.path, "w"))

    def set_if_existant_key(self, key, value):
        if key in list(self.get_options().keys()):
            section = self.get_options()[key]
            if not self.has_section(section):
                self.add_section(section)
            self.set(section, key, value)

    def set_options(self, dictionnary):
        self._options = dictionnary

    def get_options(self):
        return self._options


class Config:
    """Usage: config = Config()
    Warning: all keys must be lowercase"""

    def __init__(self):
        self._properties = {}
        self._writableParsers = []
        self.logger = logging.Logger("Config")
        self.logger.setLevel(defaultLoggingLevel)
        self.logger.addHandler(defaultLoggingHandler)

    def load_config_file(self, path):
        """load an ini config file that can't be modified."""
        parser = Parser()
        if not os.path.isfile(path):
            raise IOError(path)
        parser.read(path)
        for (option, section) in list(parser.get_options().items()):
            self.set(option, parser.get(section, option))

    def load_writable_config_file(self, writablePath, referencePath):
        """load an ini config file that can be modified."""
        #localParser exists because we din't want to copy referencePath to
        # the wirtablePath in case the config change

        #load
        parser = Parser()
        if not os.path.isfile(referencePath):
            raise IOError(referencePath)
        parser.read(referencePath)
        writableOptions = parser.get_options()
        parser.read(writablePath)

        #Write
        for (option, section) in list(writableOptions.items()):
            self.set(option, parser.get(section, option))

        #Remember
        localParser = WritableParser(writablePath)
        localParser.set_options(writableOptions)
        self._writableParsers.append(localParser)

    def get(self, key):
        """get a propertie"""
        if not key.islower():
            raise KeyError(key + " is not lowercase")
        return self._properties[key]

    def set(self, key, value):
        """set a propertie"""
        if not key.islower():
            raise KeyError(key + " is not lowercase")
        self._properties[key] = value
        for writableParser in self._writableParsers:
            writableParser.set_if_existant_key(key, value)

    def save(self):
        """Save the properties that have changed"""
        for writableParser in self._writableParsers:
            writableParser.save()

    def __str__(self):
        return str(self._properties).replace(", ", "\n")

    def __repr__(self):
        return "<Config " + str(self)[:50] + ">"
