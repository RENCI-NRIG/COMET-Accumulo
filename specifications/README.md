# COMET Design - Phase 2

## 1. Introduction

COMET is a distributed meta-data service which stores key-value oriented configuration information about resources and applications running in the distributed cloud. Clients of COMET are elements of IaaS cloud provider system, user client tools, applications running in tenant virtual systems/slices. COMET provides strong authorization controls ensuring that information is only shared with appropriate clients. 

Naming hierarchy for COMET API

- `ContextID` - GUID of slice or sliver
- `Family` (old name ‘subtype’) - user-defined string with user-imposed semantics
- `Key` (old name ‘Scope name’) - name of the ‘key’
- `Value` - single value or a map. 

## 2. COMET API operations implementation

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
