// Lab1.cpp : Defines the entry point for the application.
//

#include "framework.h"
#include "Lab1.h"

#define _USE_MATH_DEFINES
#include "math.h"

#define MAX_LOADSTRING 100

void onPaint(HWND hWnd);
void doDrawing(HWND hWnd, HDC hdc);
void drawSun(HDC hdc, float xcSun, float ycSun, float radiusSun, float radiusBeam);
void drawTriangle(HDC hdc, float xlTriangle, float ycTriangle, float heightTriangle, float widthTriangle);

// Global Variables:
HINSTANCE hInst;                                // current instance
WCHAR szTitle[MAX_LOADSTRING];                  // The title bar text
WCHAR szWindowClass[MAX_LOADSTRING];            // the main window class name

// Forward declarations of functions included in this code module:
ATOM                MyRegisterClass(HINSTANCE hInstance);
BOOL                InitInstance(HINSTANCE, int);
LRESULT CALLBACK    WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK    About(HWND, UINT, WPARAM, LPARAM);

int APIENTRY wWinMain(_In_ HINSTANCE hInstance,
                     _In_opt_ HINSTANCE hPrevInstance,
                     _In_ LPWSTR    lpCmdLine,
                     _In_ int       nCmdShow)
{
    UNREFERENCED_PARAMETER(hPrevInstance);
    UNREFERENCED_PARAMETER(lpCmdLine);

    // Initialize global strings
    LoadStringW(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
    LoadStringW(hInstance, IDC_LAB1, szWindowClass, MAX_LOADSTRING);
    MyRegisterClass(hInstance);

    // Perform application initialization:
    if (!InitInstance (hInstance, nCmdShow))
    {
        return FALSE;
    }

    HACCEL hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_LAB1));

    MSG msg;

    // Main message loop:
    while (GetMessage(&msg, nullptr, 0, 0))
    {
        if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
        {
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }

    return (int) msg.wParam;
}

//
//  FUNCTION: MyRegisterClass()
//
//  PURPOSE: Registers the window class.
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
    WNDCLASSEXW wcex;

    wcex.cbSize = sizeof(WNDCLASSEX);

    wcex.style          = CS_HREDRAW | CS_VREDRAW;
    wcex.lpfnWndProc    = WndProc;
    wcex.cbClsExtra     = 0;
    wcex.cbWndExtra     = 0;
    wcex.hInstance      = hInstance;
    wcex.hIcon          = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_LAB1));
    wcex.hCursor        = LoadCursor(nullptr, IDC_ARROW);
    wcex.hbrBackground  = (HBRUSH)(COLOR_WINDOW+1);
    wcex.lpszMenuName   = MAKEINTRESOURCEW(IDC_LAB1);
    wcex.lpszClassName  = szWindowClass;
    wcex.hIconSm        = LoadIcon(wcex.hInstance, MAKEINTRESOURCE(IDI_SMALL));

    return RegisterClassExW(&wcex);
}

//
//   FUNCTION: InitInstance(HINSTANCE, int)
//
//   PURPOSE: Saves instance handle and creates main window
//
//   COMMENTS:
//
//        In this function, we save the instance handle in a global variable and
//        create and display the main program window.
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   hInst = hInstance; // Store instance handle in our global variable

   // Налаштування власного розміру вікна
   int width = 450;
   int height = 650;
   HWND hWnd = CreateWindowW(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      200, 100, width, height, nullptr, nullptr, hInstance, nullptr);

   if (!hWnd)
   {
      return FALSE;
   }

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return TRUE;
}

//
//  FUNCTION: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  PURPOSE: Processes messages for the main window.
//
//  WM_COMMAND  - process the application menu
//  WM_PAINT    - Paint the main window
//  WM_DESTROY  - post a quit message and return
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    switch (message)
    {
    case WM_COMMAND:
        {
            int wmId = LOWORD(wParam);
            // Parse the menu selections:
            switch (wmId)
            {
            case IDM_ABOUT:
                DialogBox(hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hWnd, About);
                break;
            case IDM_EXIT:
                DestroyWindow(hWnd);
                break;
            default:
                return DefWindowProc(hWnd, message, wParam, lParam);
            }
        }
        break;
    case WM_PAINT:
        onPaint(hWnd);
        break;
    case WM_DESTROY:
        PostQuitMessage(0);
        break;
    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}

// Message handler for about box.
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
    UNREFERENCED_PARAMETER(lParam);
    switch (message)
    {
    case WM_INITDIALOG:
        return (INT_PTR)TRUE;

    case WM_COMMAND:
        if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
        {
            EndDialog(hDlg, LOWORD(wParam));
            return (INT_PTR)TRUE;
        }
        break;
    }
    return (INT_PTR)FALSE;
}

void onPaint(HWND hWnd)
{
    PAINTSTRUCT ps;
    HDC hdc = BeginPaint(hWnd, &ps);

    // Заливка фону сірим кольором
    COLORREF colorC = RGB(192, 192, 192);
    HBRUSH hBrushOld, hBrush;
    hBrush = (HBRUSH)CreateSolidBrush(colorC);
    FillRect(hdc, &ps.rcPaint, hBrush);
    DeleteObject(hBrush);

    doDrawing(hWnd, hdc);
    EndPaint(hWnd, &ps);
}

void doDrawing(HWND hWnd, HDC hdc)
{
    COLORREF colorCO = RGB(255, 0, 127);

    HPEN hPenOld, hPen;
    hPen = CreatePen(PS_SOLID, 2, colorCO);
    hPenOld = (HPEN)SelectObject(hdc, hPen);
    HBRUSH hBrushOld, hBrush;
    hBrush = (HBRUSH)CreateSolidBrush(colorCO);
    hBrushOld = (HBRUSH)SelectObject(hdc, hBrush);

    float xcSun = 220;
    float ycSun = 180;
    float radiusSun = 50;
    float radiusBeam = 100;
    drawSun(hdc, xcSun, ycSun, radiusSun, radiusBeam);

    int heightTriangle = 150;
    int widthTriangle = 140;
    drawTriangle(hdc, xcSun + 20, ycSun + radiusBeam + 40, heightTriangle, widthTriangle);

    // Видалення ручки та пензля
    SelectObject(hdc, hPenOld);
    DeleteObject(hPen);
    SelectObject(hdc, hBrushOld);
    DeleteObject(hBrush);
}

// Малювання сонечка
void drawSun(HDC hdc, float xcSun, float ycSun, float radiusSun, float radiusBeam)
{
    const int N = 16; 
    POINT ptSun[N]{};

    for (int i = 0; i < N; i++)
    {
        MoveToEx(hdc, xcSun, ycSun, 0);
        float a = (2 * M_PI) / (float)N * (float)i;
        float xBeam = xcSun + radiusBeam * cos(a);
        float yBeam = ycSun + radiusBeam * sin(a);
        LineTo(hdc, xBeam, yBeam);
        int xSun = xcSun + radiusSun * cos(a);
        int ySun = ycSun + radiusSun * sin(a);
        ptSun[i] = { xSun, ySun };
    }
    Polygon(hdc, ptSun, N);
}

// Малювання трикутника
void drawTriangle(HDC hdc, float xlTriangle, float ycTriangle, float heightTriangle, float widthTriangle)
{
    int ylTriangle = ycTriangle + heightTriangle;
    POINT ptTriangle[3] = {
        {xlTriangle, ylTriangle},
        {xlTriangle + widthTriangle, ylTriangle},
        {xlTriangle + widthTriangle / 2, ylTriangle - heightTriangle} };
    Polygon(hdc, ptTriangle, 3);
}