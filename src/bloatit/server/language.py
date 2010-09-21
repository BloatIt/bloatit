
class Language:

    language_code = {
    
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