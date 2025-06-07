var user = null;

document.addEventListener("DOMContentLoaded", function () {
  $.ajax({
    url: "profile",
    method: "GET",
    dataType: "json",
    success: function (data) {
      user = data;
      console.log("User Data:", data);
      loadUserData(data);
      renderAddresses(data);
    },
    error: function (xhr, status, error) {
      console.error("Error:", error);
    },
  });

  // let lastId = 2;
  // const user = {
  //     firstName: "Ghaidaa",
  //     email: "ghaidaa@example.com",
  //     phone: "0123456789",
  //     password: "12345678",
  //     creditLimit: 5000,
  //     addresses: [
  //         { id: 1, state: "Cairo", city: "Nasr City", street: "Street 10", department: 5},
  //         { id: 2, state: "Giza", city: "Dokki", street: "Street 15", department: 3 }
  //     ],
  // };

  // account detail tab
  const firstNameInput = document.getElementById("first-name");
  const emailInput = document.getElementById("email");
  const phoneInput = document.getElementById("phone");
  const editBtn = document.getElementById("edit-account");
  const saveBtn = document.getElementById("save-account");
  const updateMessage = document.getElementById("update-message");

  // Change Password tab
  const currentPasswordInput = document.getElementById("current-password");
  const newPasswordInput = document.getElementById("new-password");
  const confirmPasswordInput = document.getElementById("confirm-password");
  const savePasswordBtn = document.getElementById("save-password");
  const passwordUpdateMessage = document.getElementById(
    "password-update-message"
  );

  // Credit Limit tab
  const creditLimitInput = document.getElementById("credit-limit");
  const saveCreditBtn = document.getElementById("save-credit-limit");
  const creditError = document.getElementById("credit-error");
  const creditUpdateMessage = document.getElementById("credit-update-message");

  // Address tab
  const addressList = document.getElementById("address-list");
  const addAddressBtn = document.getElementById("add-address-btn");
  const addressFormOverlay = document.getElementById("address-form-overlay");
  const saveAddressBtn = document.getElementById("save-address-btn");
  const cancelAddressBtn = document.getElementById("cancel-address-btn");

  const stateInput = document.getElementById("address-state");
  const cityInput = document.getElementById("address-city");
  const streetInput = document.getElementById("address-street");
  const departmentInput = document.getElementById("address-department");

  function loadUserData(user) {
    firstNameInput.value = user.userName;
    emailInput.value = user.email;
    phoneInput.value = user.phoneNumber;
    creditLimitInput.value = user.creditLimit;
  }

  // loadUserData();

  // Buttons Handling:
  // 1- Edit (Account Details)
  editBtn.addEventListener("click", function () {
    [firstNameInput, emailInput, phoneInput].forEach((input) => {
      input.removeAttribute("disabled");
      input.style.backgroundColor = "#fff";
    });

    editBtn.classList.add("d-none");
    saveBtn.classList.remove("d-none");
  });

  // 2- Save (Account Details)
  // Save New account details Tab //
  saveBtn.addEventListener("click", function () {
    const updatedFirstName = firstNameInput.value.trim();
    const updatedEmail = emailInput.value.trim();
    const updatedPhone = phoneInput.value.trim();

    const hasChanged =
      updatedFirstName !== user.userName ||
      updatedEmail !== user.email ||
      updatedPhone !== user.phoneNumber;

      const phoneRegex = /^01[0125][0-9]{8}$/;
  if (!phoneRegex.test(updatedPhone)) {
    updateMessage.textContent = "Please enter a valid phone number.";
    updateMessage.classList.remove("d-none", "text-success");
    updateMessage.classList.add("text-danger");
    return;
  }


    [firstNameInput, emailInput, phoneInput].forEach((input) => {
      input.setAttribute("disabled", "true");
      input.style.backgroundColor = "transparent";
    });

    saveBtn.classList.add("d-none");
    editBtn.classList.remove("d-none");

    if (hasChanged) {
      $.ajax({
        url: "profile",
        method: "POST",
        data: {
          action: "updateAccount",
          firstName: updatedFirstName,
          email: updatedEmail,
          phone: updatedPhone,
        },
        success: function (response) {
          if(response.emailExists){
            updateMessage.textContent = "This email is already taken!";
          updateMessage.classList.remove("d-none", "text-success");
          updateMessage.classList.add("text-danger");
          }else {
            user.userName = updatedFirstName;
          user.email = updatedEmail;
          user.phoneNumber = updatedPhone;

          updateMessage.textContent = "Account updated successfully!";
          updateMessage.classList.remove("d-none", "text-danger");
          updateMessage.classList.add("text-success");
          //   updateMessage.classList.remove("d-none");
          setTimeout(() => {
            updateMessage.classList.add("d-none");
          }, 3000);
          }
          
        },
        error: function () {
          updateMessage.textContent = "Error updating account information.";
          updateMessage.classList.remove("d-none", "text-success");
          updateMessage.classList.add("text-danger");

          setTimeout(() => {
            updateMessage.classList.add("d-none");
          }, 3000);
          //   alert("Error updating account info.");
        },
      });

      // updateMessage.classList.remove("d-none");

      // setTimeout(() => {
      //     updateMessage.classList.add("d-none");
      // }, 3000);
    }
  });

  // 3- Save (Password)
  // Save new Password Tab //
  savePasswordBtn.addEventListener("click", function () {
    const currentPassword = currentPasswordInput.value.trim();
    const newPassword = newPasswordInput.value.trim();
    const confirmPassword = confirmPasswordInput.value.trim();

    passwordUpdateMessage.classList.remove(
      "text-danger",
      "text-success",
      "d-none"
    );

    if (!currentPassword || !newPassword || !confirmPassword) {
      passwordUpdateMessage.textContent = "Please fill in all fields!";
      passwordUpdateMessage.classList.add("text-danger");
      //   alert("Please fill in all fields!");
      return;
    }

    if (newPassword !== confirmPassword) {
      passwordUpdateMessage.textContent = "New passwords do not match!";
      passwordUpdateMessage.classList.add("text-danger");
      //   alert("New passwords do not match!");
      return;
    }

    const strongPasswordRegex = /^\w{8,12}$/;
    if (!strongPasswordRegex.test(newPassword)) {
      passwordUpdateMessage.textContent = "Password must be at least 8 characters long and include letters, numbers, and symbols.";
      passwordUpdateMessage.classList.add("text-danger");
      return;
    }

    $.ajax({
      url: "updatePassword",
      method: "POST",
      data: {
        // action: "updatePassword",
        currentPassword: currentPassword,
        newPassword: newPassword,
      },
      success: function (response) {
        passwordUpdateMessage.textContent = response;
        passwordUpdateMessage.classList.remove("text-danger");
        passwordUpdateMessage.classList.add("text-success");

        [currentPasswordInput, newPasswordInput, confirmPasswordInput].forEach(
          (input) => (input.value = "")
        );

        setTimeout(() => {
          passwordUpdateMessage.classList.add("d-none");
        }, 3000);

        //
      },
      error: function (xhr) {
        passwordUpdateMessage.textContent = xhr.responseText;
        passwordUpdateMessage.classList.remove("text-success");
        passwordUpdateMessage.classList.add("text-danger");

        setTimeout(() => {
          passwordUpdateMessage.classList.add("d-none");
        }, 3000);
      },
    });

    // user.password = newPassword;

    // [currentPasswordInput, newPasswordInput, confirmPasswordInput].forEach(
    //   (input) => {
    //     input.value = "";
    //   }
    // );

    // passwordUpdateMessage.classList.remove("d-none");

    // setTimeout(() => {
    //   passwordUpdateMessage.classList.add("d-none");
    // }, 3000);
  });

  // 4- Credit limit

  function showCreditMessage(text, isSuccess) {
    creditUpdateMessage.textContent = text;
    creditUpdateMessage.classList.remove(
      "d-none",
      "text-success",
      "text-danger"
    );
    creditUpdateMessage.classList.add(
      isSuccess ? "text-success" : "text-danger"
    );

    setTimeout(() => {
      creditUpdateMessage.classList.add("d-none");
    }, 5000);
  }
  creditLimitInput.addEventListener("input", function () {
    let value = parseInt(creditLimitInput.value, 10);

    if (isNaN(value) || value < 1 || value > 10000) {
      creditError.classList.remove("d-none");
      saveCreditBtn.classList.add("d-none");
    } else {
      creditError.classList.add("d-none");
      saveCreditBtn.classList.remove("d-none");
    }
  });

  saveCreditBtn.addEventListener("click", function () {
    let newLimit = parseInt(creditLimitInput.value, 10);

    if (isNaN(newLimit) || newLimit < 1 || newLimit > 9999) {
      showCreditMessage(
        "Invalid credit limit! Must be between 1 and 10000.",
        false
      );
      //   alert("Invalid credit limit! Must be between 1 and 10000.");
      return;
    }

    $.ajax({
      url: "profile",
      method: "POST",
      data: {
        action: "updateCredit",
        creditLimit: newLimit,
      },
      success: function () {
        user.creditLimit = newLimit;
        saveCreditBtn.classList.add("d-none");
        showCreditMessage("Credit limit updated successfully!", true);
        console.log("Credit limit now = " + user.creditLimit);
      },
      error: function () {
        showCreditMessage(
          "Error updating credit limit. Please try again later.",
          false
        );
      },
    });

    // user.creditLimit = newLimit;
    // saveCreditBtn.classList.add("d-none");
    // creditUpdateMessage.classList.remove("d-none");

    // console.log("Credit limit now = " + user.creditLimit);
    // setTimeout(() => {
    //   creditUpdateMessage.classList.add("d-none");
    // }, 3000);
  });

  // Address :
  function renderAddresses(data) {
    addressList.innerHTML = "";

    data.addresses.forEach((address) => {
      const addressDiv = document.createElement("div");
      addressDiv.className = `col-md-6 mb-3`;

      addressDiv.innerHTML = `
                <div class="address-item card p-3">
                    <p><strong>${address.state}, ${address.city}</strong></p>
                    <p>${address.street}, Dept: ${
        address.departmentNumber || "N/A"
      }</p>
                    <div class="address-actions mt-2">
                        <button class="btn btn-sm btn-danger delete-address" data-id="${
                          address.addressId
                        }">Delete</button>
                    </div>
                </div>
            `;

      addressList.appendChild(addressDiv);
    });
  }

  // renderAddresses();

  // Popup Add address
  addAddressBtn.addEventListener("click", function () {
    addressFormOverlay.classList.remove("d-none");
  });

  // Remove popup
  cancelAddressBtn.addEventListener("click", function () {
    addressFormOverlay.classList.add("d-none");
    clearForm();
  });

  // Save New Address
  saveAddressBtn.addEventListener("click", function () {
    const requiredFields = [
      { input: stateInput, message: "State is required" },
      { input: cityInput, message: "City is required" },
      { input: streetInput, message: "Street is required" },
    ];

    let isValid = true;

    requiredFields.forEach(({ input, message }) => {
      let parent = input.parentNode;
      let errorMessage = parent.querySelector(".error-message");

      if (!input.value.trim()) {
        input.classList.add("border-danger");

        if (!errorMessage) {
          let small = document.createElement("small");
          small.classList.add(
            "text-danger",
            "error-message",
            "d-block",
            "mt-1"
          );
          small.textContent = message;
          parent.appendChild(small);
        }
        isValid = false;
      } else {
        input.classList.remove("border-danger");
        if (errorMessage) {
          errorMessage.remove();
        }
      }
    });

    if (!isValid) return;

    const newAddress = {
      state: stateInput.value.trim(),
      city: cityInput.value.trim(),
      street: streetInput.value.trim(),
      departmentNumber: departmentInput.value.trim() || null,
    };

    $.ajax({
      url: "profile",
      method: "POST",
      data: {
        action: "addAddress",
        ...newAddress,
      },
      success: function (response) {
        user.addresses.push({
          ...newAddress,
          addressId: response.newId,
        });

        renderAddresses(user);
        addressFormOverlay.classList.add("d-none");
        clearForm();
      },
      error: function () {
        alert("Error while adding address");
      },
    });

    // user.addresses.push(newAddress);
    // renderAddresses();
    // addressFormOverlay.classList.add("d-none");
    // clearForm();
  });

  // Delete Selected Address
  addressList.addEventListener("click", function (e) {
    if (e.target.classList.contains("delete-address")) {
      const addressId = parseInt(e.target.getAttribute("data-id"));

      $.ajax({
        url: "profile",
        method: "POST",
        data: {
          action: "deleteAddress",
          addressId: addressId,
        },
        success: function () {
          user.addresses = user.addresses.filter(
            (address) => address.addressId !== addressId
          );
          renderAddresses(user);
        },
        error: function () {
          alert("Error while deleting address");
        },
      });

      // user.addresses = user.addresses.filter(
      //   (address) => address.id !== addressId
      // );
      // renderAddresses();
    }
  });

  const stateSelect = document.getElementById("address-state");

  new Choices(stateSelect, {
    removeItemButton: true,
    searchEnabled: true,
    shouldSort: false,
  });

  function clearForm() {
    stateInput.value = "";
    cityInput.value = "";
    streetInput.value = "";
    departmentInput.value = "";
  }
});
