# COMET Design - Phase 2

## 1. Introduction

COMET is a distributed meta-data service which stores key-value oriented configuration information about resources and applications running in the distributed cloud. Clients of COMET are elements of IaaS cloud provider system, user client tools, applications running in tenant virtual systems/slices. COMET provides strong authorization controls ensuring that information is only shared with appropriate clients. 

Naming hierarchy for COMET API

- `ContextID` - GUID of slice or sliver
- `Family` (old name ‘subtype’) - user-defined string with user-imposed semantics
- `Key` (old name ‘Scope name’) - name of the ‘key’
- `Value` - single value or a map. 

## 2. Authorization principles

Comet relies on a combination of X.509 certificates and tokens to authorize operations. Two types of client certificates are allowd:

- Valid certificates - certificates that may not be traceable to a trust root or self-signed certificates, but are otherwise valid
- Trusted certificates - valid certificates that can be traced to one of the trust roots in possession of the Comet instance

Also for some operations, a client certificate is optional. 

Two types of tokens are used:

- Read tokens - allow to perform read operations
- Write tokens - allow to overwrite/modify values

The following table summarizes the required authorization policies for the API calls that are described in the following section. 

`V = validate a client cert is trusted`
`S = specify`
`O = a valid client cert is optional`

| API Call | Semantics | Valid Cert | Trusted Cert | Read Token | Write Token |
|---|---|:---:|:---:|:---:|:---:|
| writeScope | create new scope |   |  V  |  S  |  S  |
| writeScope | modify existing scope |  O  |     |  V  |  V  |
| readScope  | read existing scope |  O  |     |  V   |    |
| enumerateScope | enumerate scopes in a context |  O  |     |  V  |    |
| deleteScope | delete existing scope |     |  V  |  V  |  V  |

The read and write token need to be strong enough to be accepted by COMET. The the tokens will need follow these rules:

* At least 8 chars
* Contains at least one digit
* Contains at least one lower alpha char and one upper alpha char
* Does not contain white spaces, tab, etc.


## 3. COMET API operations implementation

Source [Comet-Accumulo-Query-Layer](https://app.swaggerhub.com/apis/cwang/Comet-Accumulo-Query-Layer/1.0.0) specification on swaggerhub.

### writeScope

Create or modify a named scope for slice/sliver within a context, with visibility label (user\_key | comet\_admin):

- Operation requires **write** access
- Substitute existing value with new value
- What happens if scope already exists (and what if with different visibility)?

```
POST: 
	writeScope(contextID, Family, Key, Value, readToken, writeToken)

RESPONSE:
	{
	  "status": "string",
	  "message": "string",
	  "value": {},
	  "version": "string"
	}
```

### deleteScope

Delete scope within a context:

- Operation requires **write** access

```
DELETE: 
	deleteScope(contextID, Family, Key, readToken, writeToken)

RESPONSE:
	{
	  "status": "string",
	  "message": "string",
	  "value": {},
	  "version": "string"
	}
```

### readScope

Retrieve a `Value` from a named scope within a context:

- Operation requires **read** access
- Need to distinguish the following situations (discussion may be needed):
	- The scope `Value` is `null`
	- The scope existed, but was deleted
	- The scope never existed (for the period of garbage collection)
	- Scope visibility doesn’t match

```
GET: 
	readScope(contextID, Family, Key, readToken)

RESPONSE:
	{
	  "status": "string",
	  "message": "string",
	  "value": {},
	  "version": "string"
	}
```

### enumerateScope

Return a list of existing scopes within a given context:

- Operation requires **read** access
- Returns list of  `[ {Family, Key} ]` (family could be same if specified in the call; otherwise multiple families may be returned)

```
GET: 
	enumerateScopes(contextID, Family, readToken)

RESPONSE:
	{
	  "status": "string",
	  "message": "string",
	  "value": {},
	  "version": "string"
	}
```
