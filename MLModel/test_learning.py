from train import train_api

class TestLearning(object):
	def test_normal_case(self):
		result, message = train_api("./train/")
		assert result is True
		assert message == 'Training finished'

	def test_read_file_failure(self):
		result, message = train_api("./not_train/")
		assert result is False
		assert message == 'Can not read the data'