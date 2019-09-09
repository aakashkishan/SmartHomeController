import csv
from preprocess import clean

# content of test_feature_extraction.py
class TestPreprocess(object):

    # test a normal case
    def test_normal_case(self):
        path_test_demo = 'csv_for_testing/test_normal_case.csv'
        path_test_output = 'csv_for_testing/out_normal_case.csv'
        csvfile= open(path_test_demo, 'w')
        filewriter = csv.writer(csvfile)
        filewriter.writerow(['date','time','user','lightsOn'])
        filewriter.writerow(['2018-12-02', '04:00', '9351', '0'])
        filewriter.writerow(['2018-12-02', '04:00', '3440', '0'])
        filewriter.writerow(['2018-12-02', '04:00', '1688', '0'])
        csvfile.close()
        clean(path_test_demo, 
            path_test_output)
        # open file
        with open(path_test_output, 'rb') as f:
            reader = csv.reader(f)

            assert len(list(reader))==3


    # test a row with invalid date would be cleaned
    def test_invalid_date(self):
        path_test_demo = 'csv_for_testing/test_invalid_date.csv'
        path_test_output = 'csv_for_testing/out_invalid_date.csv'
        csvfile= open(path_test_demo, 'w')
        filewriter = csv.writer(csvfile)
        filewriter.writerow(['date','time','user','lightsOn'])
        filewriter.writerow(['2018-15-02', '04:00', '9351', '0'])
        csvfile.close()
        clean(path_test_demo, 
            path_test_output)
        # open file
        with open(path_test_output, 'rb') as f:
            reader = csv.reader(f)

            invalid_flag = True
            for row in reader:
                if '2018-15-02' in row:
                    invalid_flag = False
            assert invalid_flag

    # test a row with invalid time would be cleaned
    def test_invalid_time(self):
        path_test_demo = 'csv_for_testing/test_invalid_time.csv'
        path_test_output = 'csv_for_testing/out_invalid_time.csv'
        csvfile= open(path_test_demo, 'w')
        filewriter = csv.writer(csvfile)
        filewriter.writerow(['date','time','user','lightsOn'])
        filewriter.writerow(['2018-10-02', '26:00', '9351', '0'])
        csvfile.close()
        clean(path_test_demo, path_test_output)
        # open file
        with open(path_test_output, 'rb') as f:
            reader = csv.reader(f)

            invalid_flag = True
            for row in reader:
                if '26:00' in row:
                    invalid_flag = False
            assert invalid_flag

    # test the redundant data would be cleaned
    def test_redundant_data(self):
        path_test_demo = 'csv_for_testing/test_redundant_data.csv'
        path_test_output = 'csv_for_testing/out_redundant_data.csv'
        with open(path_test_demo, 'w') as csvfile:
            filewriter = csv.writer(csvfile)
            filewriter.writerow(['date','time','user','lightsOn'])
            filewriter.writerow(['2018-10-02', '21:00', '9351', '0'])
            filewriter.writerow(['2018-10-02', '21:00', '9351', '0'])
            csvfile.close()
            clean(path_test_demo, path_test_output)

            # open file
            len = 0
            with open(path_test_output, 'rb') as f:
                reader = csv.reader(f)
                for row in reader:
                    if '2018-10-02' in row and '21:00' in row \
                        and '9351' in row and '0' in row:
                        len += 1
                assert len==1

    # test the conflicting data would be cleaned
    def test_conflicting_data(self):
        path_test_demo = 'csv_for_testing/test_conflicting_data.csv'
        path_test_output = 'csv_for_testing/out_conflicting_data.csv'
        with open(path_test_demo, 'w') as csvfile:
            filewriter = csv.writer(csvfile)
            filewriter.writerow(['date','time','user','lightsOn'])
            filewriter.writerow(['2018-10-02', '21:00', '9351', '0'])
            filewriter.writerow(['2018-10-02', '21:00', '9351', '1'])
            csvfile.close()
            clean(path_test_demo, path_test_output)
            # open file
            len = 0
            with open(path_test_output, 'rb') as f:
                reader = csv.reader(f)
                for row in reader:
                    if '2018-10-02' in row and '21:00' in row \
                        and '9351' in row:
                        len += 1
                assert len==1

    # test the missing data would be cleaned
    def test_missing_data(self):
        path_test_demo = 'csv_for_testing/test_missing_data.csv'
        path_test_output = 'csv_for_testing/out_missing_data.csv'
        with open(path_test_demo, 'w') as csvfile:
            filewriter = csv.writer(csvfile)
            filewriter.writerow(['date','time','user','lightsOn'])
            filewriter.writerow(['2018-10-02', '21:00'])
            csvfile.close()
            clean(path_test_demo, path_test_output)
            # open file
            len = 0
            with open(path_test_output, 'rb') as f:
                reader = csv.reader(f)
                for row in reader:
                    if '2018-10-02' in row and '21:00' in row \
                        and '9351' in row:
                        len += 1
                assert len==0
