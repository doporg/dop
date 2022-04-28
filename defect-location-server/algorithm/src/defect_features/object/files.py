class File(object):

    def __init__(self,name, current_type):
        self.name = name
        self.block = 0
        self.critical = 0
        self.major = 0
        # current modification type like 'modify' 'add' 'delete'
        self.current_type = current_type
        self.authors = list()

    @staticmethod
    def to_dict(files_dict,file_obj):
        files_dict[file_obj.name] = file_obj

    def add_author(self,author):
        self.authors.append(author)

    def get_authors(self):
        return self.authors

    def add_block(self,block):
        self.block += block

    def get_block(self):
        return self.block

    def add_critical(self,critical):
        self.critical += critical

    def get_critical(self):
        return self.critical

    def add_major(self,major):
        self.major += major

    def get_major(self):
        return self.major

    def set_current_type(self,type):
        self.current_type = type

    def get_current_type(self):
        return self.current_type

