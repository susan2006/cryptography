import base64
from Crypto.Cipher import DES

def get_padded_bytes(text: str) -> bytes:
    raw_bytes = text.encode('utf-8')
    if len(raw_bytes) > 8:
        raise ValueError("Plaintext exceeds 8 bytes limit!")
    return raw_bytes.ljust(8, b'\x00')

def encrypt(plain_text: str, secret_key: str) -> str:
    key_bytes = secret_key.encode('utf-8')[:8]
    cipher = DES.new(key_bytes, DES.MODE_ECB)
    
    padded_plain = get_padded_bytes(plain_text)
    encrypted_bytes = cipher.encrypt(padded_plain)
    
    return base64.b64encode(encrypted_bytes).decode('utf-8')

def decrypt(cipher_text: str, secret_key: str) -> str:
    key_bytes = secret_key.encode('utf-8')[:8]
    cipher = DES.new(key_bytes, DES.MODE_ECB)
    
    encrypted_bytes = base64.b64decode(cipher_text)
    decrypted_bytes = cipher.decrypt(encrypted_bytes)
    
    return decrypted_bytes.decode('utf-8').rstrip('\x00')

if __name__ == "__main__":
    plain_text = input("Enter Plaintext Message (max 8 chars): ")
    key = input("Enter 8-character Key: ")

    if len(key) < 8:
        print("Error: DES key must be at least 8 characters long!")
    else:
        # Encrypt
        encrypted_str = encrypt(plain_text, key)
        
        print("\n--- RESULTS ---")
        print(f"Encrypted String : {encrypted_str}")

        # Decrypt
        decrypted_text = decrypt(encrypted_str, key)
        print(f"Decrypted Text   : {decrypted_text}")