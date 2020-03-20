import time
import requests

COMET_SITE = 'https://comet-hn2.exogeni.net:8222/'
EXPIRE_TIME = 6         # COMET_RECORD_EXPIRE_TIME: '6000' in docker-compose-test.yml
TTL = EXPIRE_TIME * 2  

headers = {'Accept': 'application/json'}

RECORD_1 = {
    'contextID' : 'RECORD_0_CONTEXTID',
    'family'    : 'RECORD_0_FAMILY',
    'readToken' : 'RECORD_0_READTOKEN',
    'Key'       : 'RECORD_0_KEY',
    'writeToken': 'RECORD_0_WRITETOKEN',
    'value'     : 'RECORD_0_VALUE'
}


def test_write_scope_api():
    record = RECORD_1
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'Key': record['Key'],
        'readToken': record['readToken'],
        'writeToken': record['writeToken']
    }
    response = requests.post((COMET_SITE + '/writeScope'), headers=headers, params=params,
                             data=record['value'], cert='', verify='')
    assert response.status_code == 200


def test_read_scope_api():
    record = RECORD_1
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'Key': record['Key'],
        'readToken': record['readToken'],
    }
    response = requests.get((COMET_SITE + '/readScope'), headers=headers, params=params,
                            cert='', verify='')
    json_value = (response.json()['value'])
    assert response.status_code == 200
    assert json_value['contextId'] == record['contextID']
    assert json_value['family'] == record['family']
    assert json_value['key'] == record['Key']
    assert json_value['value'] == record['value']


def test_enumerate_scope_api_with_family():
    record = RECORD_1
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'readToken': record['readToken'],
    }
    response = requests.get((COMET_SITE + '/enumerateScope'), headers=headers, params=params,
                            cert='', verify='')
    assert response.status_code == 200
    json_value = (response.json()['value'])
    assert json_value['contextId'] == record['contextID']
    assert json_value['family'] == record['family']
    found_record = False
    entries = json_value['entries']
    for entry in entries:
        if entry['key'] == record['Key'] and entry['value'] == record['value']:
            found_record = True
    assert found_record is True


def test_enumerate_scope_api():
    record = RECORD_1
    params = {
        'contextID': record['contextID'],
        'readToken': record['readToken'],
    }
    response = requests.get((COMET_SITE + '/enumerateScope'), headers=headers, params=params,
                            cert='', verify='')
    assert response.status_code == 200
    json_value = (response.json()['value'])
    assert json_value['contextId'] == record['contextID']
    print(json_value)
    found_record = False
    entries = json_value['entries']
    for entry in entries:
        if entry['family'] == record['family'] and entry['key'] == record['Key'] and entry['value'] == record['value']:
            found_record = True
    assert found_record is True


def test_delete_scope_api():
    record = RECORD_1
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'Key': record['Key'],
        'readToken': record['readToken'],
        'writeToken': record['writeToken']
    }
    response = requests.delete((COMET_SITE + '/deleteScope'), headers=headers, params=params,
                               cert='', verify='')
    assert response.status_code == 200

    # then read back, make sure empty
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'Key': record['Key'],
        'readToken': record['readToken']
    }
    response = requests.get((COMET_SITE + '/readScope'), headers=headers, params=params,
                            cert='', verify='')
    assert response.status_code == 200
    json_value = (response.json()['value'])
    assert len(json_value) == 0


def test_record_deleted_after_ttl():
    test_write_scope_api()
    time.sleep(TTL)

    record = RECORD_1
    # then read, make sure empty
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'Key': record['Key'],
        'readToken': record['readToken'],
    }
    response = requests.get((COMET_SITE + '/readScope'), headers=headers, params=params,
                            cert='', verify='')
    assert response.status_code == 200
    json_value = (response.json()['value'])
    assert len(json_value) == 0


def test_record_not_deleted_if_read_accessed():
    test_write_scope_api()
    time.sleep(1)
    test_read_scope_api()
    time.sleep(EXPIRE_TIME)
    test_read_scope_api()
    time.sleep(TTL-EXPIRE_TIME-1)
    test_read_scope_api()


def test_record_keeps_alive_if_read_accessed():
    test_write_scope_api()
    for _ in range(6):
        time.sleep(EXPIRE_TIME)
        test_read_scope_api()


def test_record_not_deleted_if_enumlate_accessed():
    test_write_scope_api()
    time.sleep(1)
    test_enumerate_scope_api()
    time.sleep(EXPIRE_TIME)
    test_enumerate_scope_api()
    time.sleep(TTL-EXPIRE_TIME-1)
    test_enumerate_scope_api()


def test_record_keeps_alive_if_enumlate_accessed():
    test_write_scope_api()
    for _ in range(6):
        time.sleep(EXPIRE_TIME)
        test_enumerate_scope_api()


def test_record_alive_then_deleted():
    test_write_scope_api()
    for _ in range(3):
        time.sleep(EXPIRE_TIME)
        test_read_scope_api()
    time.sleep(TTL)
    record = RECORD_1
    # then read, make sure empty
    params = {
        'contextID': record['contextID'],
        'family': record['family'],
        'Key': record['Key'],
        'readToken': record['readToken'],
    }
    response = requests.get((COMET_SITE + '/readScope'), headers=headers, params=params,
                            cert='', verify='')
    assert response.status_code == 200
    json_value = (response.json()['value'])
    assert len(json_value) == 0

