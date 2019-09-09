import pytest
import os
import requests

class TestServing(object):

	def test_pred_api_ok(self):
		payload = {
				'user_id': '3440', 
				'type': 'day', 
				'year' : '2019', 
				'month' : '06', 
				'day' : '21'
				}
		r = requests.get(url = 'http://17654-foxtrot.isri.cmu.edu:5000/pred', params = payload)
		assert r.status_code == 200

	def test_pred_api_missing_params(self):
		payload = {
				'user_id': '3440', 
				'type': 'day', 
				'year' : '2019', 
				'month' : '06'
				}
		r = requests.get(url = 'http://17654-foxtrot.isri.cmu.edu:5000/pred', params = payload)
		assert r.status_code != 200

	def test_train_api(self):
		r = requests.get(url = 'http://17654-foxtrot.isri.cmu.edu:5001/train')
		assert r.status_code == 200

	def test_eval_api(self):
		r = requests.get(url = 'http://17654-foxtrot.isri.cmu.edu:5001/eval')
		assert r.status_code == 200