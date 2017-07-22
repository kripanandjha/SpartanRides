#!/usr/bin/python
import os
import json
import facebook

if __name__ == '__main__' :
	token = os.environ.get('FACEBOOK_TEMP_TOKEN')

	graph = facebook.GraphAPI(token)
	profile = graph.get_object('me', fields='name, location')

	print(json.dumps(profile, indent = 4))
