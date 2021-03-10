package com.example.nurtour.di

import com.example.nurtour.data.repository.AuthRepository
import com.example.nurtour.data.repository.NotesRepository
import com.example.nurtour.data.repository.PlacesRepository
import com.example.nurtour.data.repository.UserRepository
import com.example.nurtour.domain.*
import com.example.nurtour.ui.authorized.AuthorizedViewModel
import com.example.nurtour.ui.authorized.favorite.FavoriteViewModel
import com.example.nurtour.ui.authorized.main.MainViewModel
import com.example.nurtour.ui.authorized.map.MapViewModel
import com.example.nurtour.ui.authorized.notes.NotesViewModel
import com.example.nurtour.ui.authorized.notes.dialog.newNote.NewNoteViewModel
import com.example.nurtour.ui.authorized.placeDetail.PlaceDetailViewModel
import com.example.nurtour.ui.authorized.profile.AboutViewModel
import com.example.nurtour.ui.launch.LauncherViewModel
import com.example.nurtour.ui.unauthorized.login.LoginViewModel
import com.example.nurtour.ui.unauthorized.registry.RegistryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}

val repositoryModule = module {
    factory { AuthRepository(get()) }
    factory { PlacesRepository(get()) }
    factory { UserRepository(get(), get()) }
    factory { NotesRepository(get(), get()) }
}

val useCaseModule = module {
    factory { GetMakeRegistrationUseCase(get()) }
    factory { GetMakeAuthUseCase(get()) }
    factory { GetIsAuthorizedUseCase(get()) }

    factory { GetPlaceTypeUseCase(get()) }
    factory { GetAllPlacesUseCase(get()) }
    factory { GetPlacesByTypeUseCase(get()) }

    factory { GetUserFavsUseCase(get()) }
    factory { IsUserFavUseCase(get()) }
    factory { AddUserUseCase(get()) }
    factory { AddUserFavoriteUseCase(get()) }
    factory { GetUserNameUseCase(get()) }
    factory { MakeUserLogoutUseCase(get()) }

    factory { MakeAddNotesUseCase(get()) }
    factory { GetAllUserNotesUseCase(get()) }
    factory { GetPublicNotesByPlace(get()) }
    factory { MakeAddPublicNotesUseCase(get()) }
}

val viewModelModule = module {
    viewModel { LauncherViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistryViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { NotesViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { PlaceDetailViewModel(get(), get(), get()) }
    viewModel { NewNoteViewModel(get(), get(), get()) }
    viewModel { MapViewModel(get()) }
    viewModel { AboutViewModel(get(), get()) }
    viewModel { AuthorizedViewModel(get()) }
}