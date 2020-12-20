PORT = 8080

API_URL = http://localhost:$(PORT)

# {
#   "id": "5e6f3ae2-557a-4389-bf2d-c47edd1352f0",
#   "balance": 0,
#   "locked": true
# }
test-create-account:
	@curl --silent \
			--request POST \
			--header 'Content-Type: application/json' \
			$(API_URL)/accounts | jq

# {
#   "id": "5e6f3ae2-557a-4389-bf2d-c47edd1352f0",
#   "balance": 0,
#   "locked": true
# }
test-find-account:
	@curl --silent \
			--request GET \
			--header 'Content-Type: application/json' \
			$(API_URL)/accounts/$(ACCOUNT_ID) | jq

# {
# }
test-unlock-account:
	@curl --silent \
			--request PATCH \
			--header 'Content-Type: application/json' \
			$(API_URL)/accounts/$(ACCOUNT_ID)/unlock | jq

# {
# }
test-lock-account:
	@curl --silent \
			--request PATCH \
			--header 'Content-Type: application/json' \
			$(API_URL)/accounts/$(ACCOUNT_ID)/lock | jq

# {
#   "balance": 25
# }
test-deposit:
	@curl --silent \
			--request POST \
			--header 'Content-Type: application/json' \
			--data '{"amount":25.00}' \
			$(API_URL)/accounts/$(ACCOUNT_ID)/deposit | jq

# {
#   "balance": 13.75
# }
test-draw:
	@curl --silent \
			--request POST \
			--header 'Content-Type: application/json' \
			--data '{"amount":11.25}' \
			$(API_URL)/accounts/$(ACCOUNT_ID)/draw | jq

# {
#   "errors": [
#     {
#       "field": "locked",
#       "message": "Account.Operation.Money.isLocked",
#       "metadata": []
#     },
#     {
#       "field": "balance",
#       "message": "Account.Operation.Draw.hasEnoughBalance",
#       "metadata": []
#     }
#   ]
# }
test-draw-validations:
	@curl --silent \
			--request GET \
			--header 'Content-Type: application/json' \
			$(API_URL)/accounts/$(ACCOUNT_ID)/draw/validations | jq

