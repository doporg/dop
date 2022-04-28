from .git_numstat import merge_modify_pattern


class RawGitNameStat:
    def __init__(self, is_merge):
        self.is_merge = is_merge
        self.commit_id_line = None
        self.file_lines = None

    def extract_id(self):
        if not self.is_merge:
            return self.commit_id_line[-40:]
        else:
            return self.commit_id_line[0:40]

    def extract_parent_id(self):
        if self.is_merge and merge_modify_pattern.match(self.commit_id_line) is not None:
            return self.commit_id_line[47:87]
        return None


class GitNameStat:
    """
    git file name stat [{'modified_path': 'pf4j/src/main/java/ro/fortsoft/pf4j/processor/LegacyExtensionStorage.java', 'type': 'modify'},
    {'modified_path': 'pf4j/src/main/java/ro/fortsoft/pf4j/processor/ServiceProviderExtensionStorage.java', 'type': 'modify'}]
    git file modify type {'pf4j/src/main/java/ro/fortsoft/pf4j/processor/LegacyExtensionStorage.java': 'modify',
    'pf4j/src/main/java/ro/fortsoft/pf4j/processor/ServiceProviderExtensionStorage.java': 'modify'}

    """
    def __init__(self, project):
        self.project = project
        self.commit_id = None
        self.is_merge = False
        self.file_name_stat = None
        self.base_commit = None
        self.__cache_file_modify_map = None

    def from_raw_git_namestat(self, rgn):
        assert(isinstance(rgn, RawGitNameStat))
        self.commit_id = rgn.extract_id()
        self.is_merge = rgn.is_merge
        # parent commit id
        self.base_commit = rgn.extract_parent_id()
        self.file_name_stat = list()
        for l in rgn.file_lines:
            f_dict = dict()
            groups = l.split('\t')
            if len(groups) == 3:
                assert(groups[0][0] == 'R')
                f_dict['type'] = 'rename'
                f_dict['modified_path'] = groups[1]
                f_dict['current_path'] = groups[2]
            else:
                assert(len(groups) == 2)
                if groups[0] == 'A':
                    f_dict['type'] = 'add'
                    f_dict['modified_path'] = groups[1]
                elif groups[0] in ['M','T']:
                    f_dict['type'] = 'modify'
                    f_dict['modified_path'] = groups[1]
                else:
                    assert(groups[0] == 'D')
                    f_dict['type'] = 'delete'
                    f_dict['modified_path'] = groups[1]
            self.file_name_stat.append(f_dict)

    @property
    def file_modify_type(self):
        if self.__cache_file_modify_map is None:
            f_type_dict = dict()
            for f in self.file_name_stat:
                f_type_dict[f['modified_path']] = f['type']
            self.__cache_file_modify_map = f_type_dict
        return self.__cache_file_modify_map



