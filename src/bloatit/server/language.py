# -*- coding: utf-8 -*-

# Copyright (C) 2010 BloatIt.
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

import gettext
from bloatit.config import config

class Language:

    language_code = {
        "en" : "en",
        "en-us" : "en",
        "fr" : "fr",
        "fr-fr" : "fr"

    }

    """static $languageList = array(
    'en' => array('key' => 'en_US.utf8', 'choosable' => True, 'label' => 'English', 'code' => 'en'),
    'fr' => array('key' => 'fr_FR.utf8', 'choosable' => True, 'label' => 'Français', 'code' => 'fr'),
    'fr-fr' => array('key' => 'fr_FR.utf8', 'choosable' => False, 'ref' => 'fr'),
    'fr-be' => array('key' => 'fr_FR.utf8', 'choosable' => False, 'ref' => 'fr'),
    'fr-ca' => array('key' => 'fr_FR.utf8', 'choosable' => False, 'ref' => 'fr'),
    'fr-lu' => array('key' => 'fr_FR.utf8', 'choosable' => False, 'ref' => 'fr'),
    'fr-ch' => array('key' => 'fr_FR.utf8', 'choosable' => False, 'ref' => 'fr'),
    );"""

    def __init__(self):
        self.code = "en"

    def get_code(self):
        return self.code

    def set_by_code(self, code):
        self.code = code

    def find_preferred(self, preferred_langs):
        for preferred_lang in preferred_langs:
            lang = preferred_lang.split(";")[0]
            if lang in self.language_code:
                self.code = self.language_code[lang]
                break
            else:
                print("Unknow language code "+lang)
                #TODO: clean log

    def get_gettext(self):
        lang = gettext.translation(config.get("gettext_package"), config.get("localedir"), languages=[self.code], codeset="UTF-8")
        return lang.gettext

