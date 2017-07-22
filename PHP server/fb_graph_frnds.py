#!/usr/bin/python
import os
import json
import facebook

if __name__ == '__main__' :
	token = os.environ.get('FACEBOOK_TEMP_TOKEN')

	graph = facebook.GraphAPI(token)
	#user = graph.get_object("me")
	user = graph.get_object("me",fields='likes.limit(1000)')
	friends = graph.get_connections(user['id'], 'friends')
	#location = graph.get_(user['id'], 'location')
	#print(json.dumps(friends, indent = 4))
	#print(json.dumps(user, indent = 4))
	test = json.loads(json.dumps(user))
	#print(json.dumps(user, indent=4))
	#print json.dumps(test["likes"],indent=4)
	idd = test['likes']
	idd = idd['data']
	for row in range(len(idd)):
		print idd[row]['id']
