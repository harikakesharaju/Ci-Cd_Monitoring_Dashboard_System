# Postman API Testing Guide

## Quick Start

1. **Download Postman** from [postman.com](https://www.postman.com/downloads/)

2. **Import Collection:**
   - Open Postman
   - Click `File` → `Import`
   - Select `POSTMAN_COLLECTION.json` from this directory
   - Click `Import`

3. **Configure Environment Variables:**
   - In Postman, look for the `Variables` tab in the collection
   - Set `base_url` = `http://localhost:8080/api` (adjust port/host as needed)
   - Leave `auth_token` empty (it will be filled after login)

---

## Testing Sequence (Recommended Order)

### 1️⃣ **Authentication Setup** (MUST DO FIRST)
```
1. POST /auth/register → Create a test user
   - Change username/email/password as needed
   
2. POST /auth/login → Get JWT token
   - Copy the token from response
   - Paste it in the {{auth_token}} variable
   
✅ All future requests will use this token
```

---

### 2️⃣ **User Management** (Test RBAC)
```
1. GET /auth/users → List all users
2. GET /auth/users/{id} → Get specific user
3. PUT /auth/users/{id} → Update user role to ADMIN
4. DELETE /auth/users/{id} → Delete user
```

**Key Points:**
- Test both success (valid user) and failure (non-existent user) cases
- Verify RBAC: some endpoints require ADMIN role
- Check error responses (401, 403)

---

### 3️⃣ **Project Management** (Foundation for builds/deployments)
```
1. POST /projects → Create a test project
   - Use real GitHub repo URL
   - Match Jenkins job name
   
2. GET /projects → List projects
3. GET /projects/{id} → Get project details
4. PUT /projects/{id} → Update project config
5. DELETE /projects/{id} → Delete project
```

---

### 4️⃣ **Builds** (Core CI/CD workflow)
```
1. POST /builds → Create build (manually)
   - Use project ID from step 3
   - Test both SUCCESS and FAILED statuses
   
2. GET /builds → List all builds
3. GET /builds/{id} → Get build details
4. GET /builds/project/{projectId} → Get builds for specific project
5. PUT /builds/{id} → Update build status
6. DELETE /builds/{id} → Delete build
```

**Critical Test:**
```
- Trigger real GitHub webhook → Should create build automatically
- POST /webhook/github → Use test payload provided
- Verify it triggers Jenkins build
```

---

### 5️⃣ **Deployments** (After builds)
```
1. POST /deployments → Create deployment
   - Use buildId from builds
   - Test STAGING and PRODUCTION environments
   
2. GET /deployments → List deployments
3. GET /deployments/{id} → Get deployment details
4. PUT /deployments/{id} → Update status (IN_PROGRESS → SUCCESS/FAILED)
5. DELETE /deployments/{id} → Delete deployment
```

**Test Full Workflow:**
```
Build #1 (SUCCESS)
   ↓
Deploy to STAGING
   ↓
Deploy to PRODUCTION
```

---

### 6️⃣ **Logs** (Debugging & audit trail)
```
1. GET /logs → List all logs (with pagination)
2. GET /logs/{id} → Get specific log
3. GET /logs/build/{buildId} → Get logs for specific build
4. PUT /logs/{id} → Update log (if needed)
5. DELETE /logs/{id} → Delete log
```

**Important:**
- Test pagination: `?page=0&size=10`
- Verify logs are captured from builds
- Check log levels (INFO, ERROR, DEBUG)

---

### 7️⃣ **Metrics** (Dashboard data)
```
1. GET /metrics/failure-rate/{projectId} → Get failure %
2. GET /metrics/success-rate/{projectId} → Get success %
```

**Verify:**
- Calculations are correct based on builds
- Handles projects with no builds (return 0% or N/A)

---

### 8️⃣ **Dashboard** (Overview endpoint)
```
1. GET /dashboard → Overall system metrics
2. GET /dashboard/project/{projectId} → Project-specific metrics
```

**Should Include:**
- Total projects, builds, deployments
- Success/failure rates
- Recent activity

---

## Common Testing Scenarios

### ✅ Happy Path (Everything works)
```json
Register → Login → Create Project → Create Build → Deploy to Staging → Deploy to Production → View Metrics
```

### ⚠️ Error Cases (Test error handling)
```
❌ POST /auth/login with wrong password → 401 UNAUTHORIZED
❌ GET /builds/999 (non-existent) → 404 NOT FOUND
❌ POST /deployments without buildId → 400 BAD REQUEST
❌ DELETE /projects/{id} when builds exist → Should handle gracefully
❌ POST /webhook/github with invalid signature → 401 UNAUTHORIZED
```

### 🔐 Security Tests
```
❌ Call any protected endpoint without {{auth_token}} → 401
❌ Call /auth/users (ADMIN only) as regular USER → 403
❌ Update another user's profile as regular user → 403
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| **401 Unauthorized** | Token expired or missing. Run login again and update {{auth_token}} |
| **403 Forbidden** | Your user role lacks permission. Update user role to ADMIN in DB or via PUT /auth/users/{id} |
| **404 Not Found** | Resource doesn't exist. Check ID or create resource first |
| **500 Internal Server** | Check backend logs. May be database or configuration issue |
| **Webhook not triggering build** | Verify Jenkins URL in project, check webhook secret, review backend logs |

---

## Environment-Specific Testing

### Local Development
```
base_url: http://localhost:8080/api
```

### Staging
```
base_url: http://staging.example.com/api
```

### Production
```
base_url: https://api.example.com
Note: Be careful! Don't create test data in production
```

---

## Tips for Frontend Development

Once you validate all APIs with Postman:
- ✅ Auth flow works → Build login/register pages
- ✅ Projects/Builds work → Build dashboard
- ✅ Deployments work → Build deployment management UI
- ✅ Metrics work → Build charts/analytics
- ✅ Webhooks work → System can auto-update without manual API calls

---

## Next Steps

1. **Import this collection into Postman**
2. **Follow testing sequence above**
3. **Document any issues/failures**
4. **Once all tests pass → Start frontend development**

Good luck! 🚀
