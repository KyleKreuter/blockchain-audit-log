
# Blockchain based Audit Log (Proof of Concept)
This repository contains a **Proof of Concept (PoC)** for a blockchain-based  
audit log implemented in Java. The project demonstrated how blockchain technology  
can be utilized to create an immutable, secure and transparentlogging system

## Proof of Work in an Audit Log
[**Proof of Work (PoW)**](https://de.wikipedia.org/wiki/Proof_of_Work) is a mechanism used in blockchain systems to ensure that  
adding a new block to the chain requires computational effort. This prevents malicious  
actors from easily tampering with the chain and ensures the integrity of the  
blockchain. In this repository, a simplified version of PoW is implemented to simulate  
the concept and demonstrate how it can be applied to an audit log.

### Implementation
* Each block contains a nonce, a number that must be calculated to satisfy a predefined  
condition (e.g., the hash of the block must start with a certain number of zeros).  
* The PoW process involves incrementing the nonce and recalculating the hash until  
the condition is met.
* This computational effort makes it infeasible to manipulate blocks without significant resources.

*While the PoW in this project is simplified and does not involve actual distributed mining, it demonstrates the foundational concept and how it could be adapted for securing audit logs.*

### Test Coverage
* Tests cover block creation, blockchain integrity, and tamper detection.
* Ensure all tests pass to verify the system's integrity.  

## License
This project is licensed under the MIT License. See the [LICENSE](https://github.com/KyleKreuter/proof-of-work-audit-log/blob/main/LICENSE) file for details.
